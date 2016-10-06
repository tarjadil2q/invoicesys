package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.PukItemMeasurement;
import com.pce.domain.dto.ApiError;
import com.pce.domain.dto.DomainObjectDTO;
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
import org.springframework.hateoas.*;
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
@RequestMapping("/api/v1/pce/pukitemmeasurement")
@ExposesResourceFor(PukItemMeasurementDto.class)
public class PukItemMeasurementController {
  private static final Logger LOG = LoggerFactory.getLogger(PukItemMeasurementController.class);

  public static final String PUK_ITEM_MEASUREMENT_URL_PATH = "/pukitemmeasurement";

  private PukItemMeasurementService pukItemMeasurementService;
  private PukItemMeasurementMapper pukItemMeasurementMapper;
  private EntityLinks entityLinks;
  private PagedResourcesAssembler assembler;


  @Autowired
  public PukItemMeasurementController(PukItemMeasurementService pukItemMeasurementService, PukItemMeasurementMapper pukItemMeasurementMapper,
                                      EntityLinks entityLinks,
                                      PagedResourcesAssembler assembler) {
    this.pukItemMeasurementService = pukItemMeasurementService;
    this.pukItemMeasurementMapper = pukItemMeasurementMapper;
    this.entityLinks = entityLinks;
    this.assembler = assembler;
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createPukItemMeasurement(@RequestBody @Valid PukItemMeasurementDto pukItemMeasurementDto) {

    PukItemMeasurement pukItemMeasurement = pukItemMeasurementMapper.mapDtoIntoEntity(pukItemMeasurementDto);
    List<PukItemMeasurement> pukItemsMeasurementFound = pukItemMeasurementService.findPukItemMeasurementByTypeOfMeasurementIgnoreCase(pukItemMeasurementDto.getTypeOfMeasurement());
    if (!CollectionUtils.isEmpty(pukItemsMeasurementFound)) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.CONFLICT,
              "Puk item measurement already exists, please enter different Puk item measurement name", Lists.newArrayList("Puk item measurement already exists"))), HttpStatus.CONFLICT);
    }

    pukItemMeasurementService.createOrUpdatePukItemMeasurement(pukItemMeasurement);
    pukItemMeasurementDto.add(ControllerLinkBuilder.linkTo(PukItemMeasurementController.class).slash(pukItemMeasurement.getPukItemMeasurementId()).withRel(PUK_ITEM_MEASUREMENT_URL_PATH).withSelfRel());
    return ControllerHelper.getResponseEntityWithoutBody(pukItemMeasurementDto, HttpStatus.CREATED);
  }


  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPukItemMeasurementById(@PathVariable Long id) {
    PukItemMeasurement pukItemMeasurement = pukItemMeasurementService.getPukItemMeasurementById(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk Item Measurement=%s not found", id)));
    PukItemMeasurementDto pukMeasurementDto = (PukItemMeasurementDto) pukItemMeasurementMapper.mapEntityIntoDto(pukItemMeasurement);
    Link linkForPukMeasurement = entityLinks.linkToSingleResource(PukItemMeasurementDto.class, pukMeasurementDto.getPukItemMeasurementId());
    Resource<DomainObjectDTO> pukMeasurementResource = new Resource<>(pukMeasurementDto, linkForPukMeasurement);
    return new ResponseEntity<>(pukMeasurementResource, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPukItemMeasurements(Pageable pageRequest) {
    Page<PukItemMeasurement> allPukMeasurements = pukItemMeasurementService.getAllAvailablePukItemMeasurement(pageRequest);
    Page<DomainObjectDTO> pukMeasurementDtos = pukItemMeasurementMapper.mapEntityPageIntoDTOPage(pageRequest, allPukMeasurements);
    return new ResponseEntity<>(assembler.toResource(pukMeasurementDtos), HttpStatus.OK);
  }


  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePukItemMeasurement(@PathVariable("id") long id,
                                                                        @RequestBody @Valid PukItemMeasurementDto pukItemMeasurementDto) {
    Optional<PukItemMeasurement> currentPukItemMeasurement = pukItemMeasurementService.getPukItemMeasurementById(id);

    if (!currentPukItemMeasurement.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Puk Measurement to be updated not found, please check id is correct ", "Puk measurement id is not found")), HttpStatus.NOT_FOUND);
    }
    PukItemMeasurement toBeUpdatedPukMeasurement = currentPukItemMeasurement.get();

    PukItemMeasurement pukItemMeasurement = pukItemMeasurementMapper.mapDtoIntoEntity(pukItemMeasurementDto);

    toBeUpdatedPukMeasurement.setTypeOfMeasurement(pukItemMeasurement.getTypeOfMeasurement());

    PukItemMeasurement updatedPukItemMeasurement = pukItemMeasurementService.createOrUpdatePukItemMeasurement(toBeUpdatedPukMeasurement);
    pukItemMeasurementDto.add(ControllerLinkBuilder.linkTo(PukItemMeasurementController.class).slash(updatedPukItemMeasurement.getPukItemMeasurementId()).withRel(PUK_ITEM_MEASUREMENT_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukItemMeasurementDto, HttpStatus.OK);
  }
}
