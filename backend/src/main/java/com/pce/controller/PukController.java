package com.pce.controller;

import com.pce.domain.Puk;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukDto;
import com.pce.service.PukService;
import com.pce.service.mapper.PukItemMapper;
import com.pce.service.mapper.PukMapper;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.PukCreateValidator;
import com.pce.validation.validator.PukUpdateValidator;
import com.pce.validation.validator.ValidationErrorBuilder;
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
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@RestController
@RequestMapping("/api/pce/puk")
@ExposesResourceFor(PukDto.class)
public class PukController {

  private static final Logger LOG = LoggerFactory.getLogger(PukController.class);

  private PukService pukService;
  private PukMapper pukMapper;
  private EntityLinks entityLinks;
  private PukItemMapper pukItemMapper;
  @Autowired
  private PukCreateValidator pukCreateValidator;

  @Autowired
  private PukUpdateValidator pukUpdateValidator;


  public static final String PUK_URL_PATH = "/puk";

  @Autowired
  public PukController(PukService pukService, PukMapper pukMapper, EntityLinks entityLinks,
                       PukItemMapper pukItemMapper) {
    this.pukService = pukService;
    this.pukMapper = pukMapper;
    this.entityLinks = entityLinks;
    this.pukItemMapper = pukItemMapper;
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
  public HttpEntity<Resource<DomainObjectDTO>> createPuk(@RequestBody @Valid PukDto pukDto, Errors errors) {
    ValidationUtils.invokeValidator(pukCreateValidator, pukDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }
    Puk puk = pukMapper.mapDtoIntoEntity(pukDto);

    Puk createdPuk = pukService.createOrUpdatePuk(puk);
    pukDto.add(ControllerLinkBuilder.linkTo(PukController.class).slash(createdPuk.getPukId()).withRel(PUK_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukDto, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePuk(@PathVariable("id") long id,
                                                         @RequestBody @Valid PukDto pukDto, Errors errors) {

    Puk puk = pukService.getPukByPukId(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk=%s not found", id)));

    ValidationUtils.invokeValidator(pukUpdateValidator, pukDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    Puk mappedPuk = pukMapper.mapDtoIntoEntity(pukDto);
    mappedPuk.setPukId(puk.getPukId());
    Puk updatedPuk = pukService.createOrUpdatePuk(mappedPuk);

    pukDto.add(ControllerLinkBuilder.linkTo(PukController.class).slash(updatedPuk.getPukId()).withRel(PUK_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukDto, HttpStatus.OK);

  }


}
