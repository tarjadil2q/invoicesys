package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.PukItemMeasurement;
import com.pce.domain.dto.ApiError;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukGroupDto;
import com.pce.domain.dto.PukItemMeasurementDto;
import com.pce.service.PukItemMeasurementService;
import com.pce.service.mapper.PukItemMeasurementMapper;
import com.pce.util.ControllerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 23/08/2016.
 */
@RestController
@RequestMapping("/api/pce/pukitemmeasurement")
public class PukItemMeasurementController {
  private static final Logger LOG = LoggerFactory.getLogger(PukItemMeasurementController.class);

  public static final String PUK_ITEM_MEASUREMENT_URL_PATH = "/pukitemmeasurement";

  private PukItemMeasurementService pukItemMeasurementService;
  private PukItemMeasurementMapper pukItemMeasurementMapper;
  private EntityLinks entityLinks;


  @Autowired
  public PukItemMeasurementController(PukItemMeasurementService pukItemMeasurementService, PukItemMeasurementMapper pukItemMeasurementMapper, EntityLinks entityLinks) {
    this.pukItemMeasurementService = pukItemMeasurementService;
    this.pukItemMeasurementMapper = pukItemMeasurementMapper;
    this.entityLinks = entityLinks;
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createPukItemMeasurment(@RequestBody @Valid PukItemMeasurementDto pukItemMeasurementDto) {

    PukItemMeasurement pukItemMeasurement = pukItemMeasurementMapper.mapDtoIntoEntity(pukItemMeasurementDto);
    List<PukItemMeasurement> pukItemsMeasurementFound = pukItemMeasurementService.findPukItemMeasurementByTypeOfMeasurementIgnoreCase(pukItemMeasurementDto.getTypeOfMeasurement());
    if (!CollectionUtils.isEmpty(pukItemsMeasurementFound)) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.CONFLICT,
              "Puk item measurement already exists, please enter different Puk item measurement name", Lists.newArrayList("Puk item measurement already exists"))), HttpStatus.CONFLICT);
    }

    pukItemMeasurementService.createOrUpdatePukItemMeasurement(pukItemMeasurement);
    pukItemMeasurementDto.add(ControllerLinkBuilder.linkTo(RoleController.class).slash(pukItemMeasurement.getId()).withRel(PUK_ITEM_MEASUREMENT_URL_PATH).withSelfRel());
    return ControllerHelper.getResponseEntityWithoutBody(pukItemMeasurementDto, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPukItemMeasurementById(@PathVariable Long id) {
    PukItemMeasurement pukItemMeasurement = pukItemMeasurementService.getPukItemMeasurementById(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk Item Measurement=%s not found", id)));
    PukGroupDto pukGroupDto = (PukGroupDto) pukItemMeasurementMapper.mapEntityIntoDto(pukItemMeasurement);
    Link linkForPUkGroup = entityLinks.linkToSingleResource(PukGroupDto.class, pukGroupDto.getPukGroupId());
    Resource<DomainObjectDTO> userResource = new Resource<>(pukGroupDto, linkForPUkGroup);
    return new ResponseEntity<>(userResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPukItemMeasurements(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<PukItemMeasurement> allPukGroups = pukItemMeasurementService.getAllAvailablePukItemMeasurement(pageRequest);
    Page<DomainObjectDTO> pukGroupDtos = pukItemMeasurementMapper.mapEntityPageIntoDTOPage(pageRequest, allPukGroups);
    return new ResponseEntity<>(assembler.toResource(pukGroupDtos), HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePukItemMeasurement(@PathVariable("id") long id,
                                                                        @RequestBody @Valid PukItemMeasurementDto pukItemMeasurementDto) {
    Optional<PukItemMeasurement> currentPukItemMeasurement = pukItemMeasurementService.getPukItemMeasurementById(id);

    if (!currentPukItemMeasurement.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Puk Group to be updated not found, please check id is correct ", "Puk Group id is not found")), HttpStatus.NOT_FOUND);
    }

    PukItemMeasurement pukItemMeasurement = pukItemMeasurementMapper.mapDtoIntoEntity(pukItemMeasurementDto);


    PukItemMeasurement updatedPukItemMeasurement = pukItemMeasurementService.createOrUpdatePukItemMeasurement(pukItemMeasurement);
    pukItemMeasurementDto.add(ControllerLinkBuilder.linkTo(RoleController.class).slash(updatedPukItemMeasurement.getId()).withRel(PUK_ITEM_MEASUREMENT_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukItemMeasurementDto, HttpStatus.OK);
  }
}
