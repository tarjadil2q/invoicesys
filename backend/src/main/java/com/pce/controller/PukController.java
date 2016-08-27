package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.Puk;
import com.pce.domain.PukGroup;
import com.pce.domain.PukItem;
import com.pce.domain.PukItemMeasurement;
import com.pce.domain.dto.ApiError;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukDto;
import com.pce.domain.dto.PukItemDto;
import com.pce.service.PukGroupService;
import com.pce.service.PukItemMeasurementService;
import com.pce.service.PukService;
import com.pce.service.mapper.PukItemMapper;
import com.pce.service.mapper.PukMapper;
import com.pce.util.ControllerHelper;
import com.pce.validation.PukItemMeasurementAssoc;
import com.pce.validation.group.NewPuk;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@RestController
@RequestMapping("/api/pce/puk")
@ExposesResourceFor(PukDto.class)
public class PukController {

  private static final Logger LOG = LoggerFactory.getLogger(PukController.class);

  private PukService pukService;
  private PukGroupService pukGroupService;
  private PukMapper pukMapper;
  private EntityLinks entityLinks;
  private PukItemMapper pukItemMapper;
  private PukItemMeasurementService pukItemMeasurementService;
  private static final String PUK_URL_PATH = "/puk";

  @Autowired
  public PukController(PukService pukService, PukMapper pukMapper, EntityLinks entityLinks,
                       PukGroupService pukGroupService,
                       PukItemMeasurementService pukItemMeasurementService,
                       PukItemMapper pukItemMapper) {
    this.pukService = pukService;
    this.pukMapper = pukMapper;
    this.entityLinks = entityLinks;
    this.pukGroupService = pukGroupService;
    this.pukItemMapper = pukItemMapper;
    this.pukItemMeasurementService = pukItemMeasurementService;
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPukById(@PathVariable Long id) {

    Puk puk = pukService.getPukByPukId(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk=%s not found", id)));
    PukDto pukDto = (PukDto) pukMapper.mapEntityIntoDto(puk);
    Link linkForPuk = entityLinks.linkToSingleResource(PukDto.class, pukDto.getPukId());
    Resource<DomainObjectDTO> pukResource = new Resource<>(pukDto, linkForPuk);
    return new ResponseEntity<>(pukResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPuks(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<Puk> allPuks = pukService.getAllAvailablePuk(pageRequest);
    Page<DomainObjectDTO> pukDtos = pukMapper.mapEntityPageIntoDTOPage(pageRequest, allPuks);
    return new ResponseEntity<>(assembler.toResource(pukDtos), HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createPuk(@RequestBody @Validated(NewPuk.class) PukDto pukDto) {

    Puk puk = pukMapper.mapDtoIntoEntity(pukDto);
    long pukGroupId = pukDto.getPukGroup().getPukGroupId();
    PukGroup pukGroupById = pukGroupService.getPukGroupById(pukGroupId).orElseThrow(() -> new NoSuchElementException(String.format("Puk group = %s not found", pukGroupId)));


    List<PukItemDto> pukItemsDtos = pukDto.getPukItems();


    Set<PukItem> pukItems = pukItemsDtos.stream().map(pukItemDto -> pukItemMapper.mapDtoIntoEntity(pukItemDto))
            .collect(Collectors.toSet());


    for (PukItem pukItem : pukItems){
      long pukItemMeasurementId = pukItem.getPukItemMeasurement().getPukItemMeasurementId();
      Optional<PukItemMeasurement> pukItemMeasurementById = pukItemMeasurementService.getPukItemMeasurementById(pukItemMeasurementId);
      if (!pukItemMeasurementById.isPresent()){
        return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.BAD_REQUEST,
                "Puk item measurement" + pukItemMeasurementId + " for " + pukItem.getActivityName() + "  cannot be found, please provide valid puk item measurement id", Lists.newArrayList("Invalid puk item measurement id"))), HttpStatus.BAD_REQUEST);
      }
      pukItem.setPukItemMeasurement(pukItemMeasurementById.get());
    }
    puk.setPukGroup(pukGroupById);
    puk.setPukItems(pukItems);
    Puk createdPuk = pukService.createOrUpdatePuk(puk);
    pukDto.add(ControllerLinkBuilder.linkTo(PukController.class).slash(createdPuk.getPukId()).withRel(PUK_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukDto, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePuk(@PathVariable("id") long id,
                                                         @RequestBody @Validated PukDto pukDto) {
    Optional<Puk> currentPuk = pukService.getPukByPukId(id);

    if (!currentPuk.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Puk to be updated not found, please check id is correct ", "Puk id is not found")), HttpStatus.NOT_FOUND);
    }
    Puk toBeUpdatePuk = currentPuk.get();

    Puk pukUpdateSource = pukMapper.mapDtoIntoEntity(pukDto);
    toBeUpdatePuk.setPukNo(pukUpdateSource.getPukNo());
    toBeUpdatePuk.setPukDescription(pukUpdateSource.getPukDescription());
    if (pukUpdateSource.getPukGroup().getPukGroupId() == pukUpdateSource.getPukGroup().getPukGroupId()){

    }
/*
    Puk updatedPuk = pukService.createOrUpdatePuk(puk);
    pukDto.add(ControllerLinkBuilder.linkTo(PukController.class).slash(updatedPuk.getPukId()).withRel(PUK_URL_PATH).withSelfRel());*/

    return ControllerHelper.getResponseEntityWithoutBody(pukDto, HttpStatus.OK);

  }
}
