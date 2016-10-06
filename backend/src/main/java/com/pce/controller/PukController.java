package com.pce.controller;

import com.pce.domain.Pce;
import com.pce.domain.Puk;
import com.pce.domain.PukGroup;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukDto;
import com.pce.service.PukService;
import com.pce.service.mapper.PukItemMapper;
import com.pce.service.mapper.PukMapper;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.PukCreateValidator;
import com.pce.validation.validator.PukUpdateValidator;
import com.pce.validation.validator.ValidationErrorBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/puk")
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

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PagedResourcesAssembler assembler;


  public static final String PUK_URL_PATH = "/puk";

  @Autowired
  public PukController(PukService pukService, PukMapper pukMapper, EntityLinks entityLinks,
                       PukItemMapper pukItemMapper) {
    this.pukService = pukService;
    this.pukMapper = pukMapper;
    this.entityLinks = entityLinks;
    this.pukItemMapper = pukItemMapper;
  }


  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<PukDto>> getPukById(@PathVariable Long id) {

    Puk puk = pukService.getPukByPukId(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk=%s not found", id)));
    return new ResponseEntity<>(mappedPuk(puk), HttpStatus.OK);
  }


  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPuks(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<Puk> allPuks = pukService.getAllAvailablePuk(pageRequest);
    Page<Resource<PukDto>> newPaged = allPuks.map(source -> mappedPuk(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @RequestMapping(value = "/pukgroup/{pukGroupId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPuksByPukGroup(@PathVariable("pukGroupId") long id,
                                                                       Pageable pageRequest) {
    PukGroup pukGroup = new PukGroup();
    pukGroup.setPukGroupId(id);
    Page<Puk> allPuks = pukService.getPuksForPukGroup(pukGroup, pageRequest);
    Page<Resource<PukDto>> newPaged = allPuks.map(source -> mappedPuk(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }


  @PreAuthorize("@pukUserServiceImpl.canCurrentUserCreateOrUpdatePuk(principal)")
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


  @PreAuthorize("@pukUserServiceImpl.canCurrentUserCreateOrUpdatePuk(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePuk(@PathVariable("id") long id,
                                                         @RequestBody @Valid PukDto pukDto, Errors errors) {

    pukDto.setPukId(id);
    ValidationUtils.invokeValidator(pukUpdateValidator, pukDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    Puk mappedPuk = pukMapper.mapDtoIntoEntity(pukDto);
    mappedPuk.setPukId(id);
    Puk updatedPuk = pukService.createOrUpdatePuk(mappedPuk);

    pukDto.add(ControllerLinkBuilder.linkTo(PukController.class).slash(updatedPuk.getPukId()).withRel(PUK_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukDto, HttpStatus.OK);

  }

  @RequestMapping(value = "/pce/{pceId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPukByPceId(@PathVariable("pceId") long id) {
    Pce pce = new Pce();
    pce.setPceId(id);
    Optional<Puk> puk = pukService.getPukByPce(pce);
    if (!puk.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    PukDto pukDto = modelMapper.map(puk.get(), PukDto.class);
    Link linkForPuk = entityLinks.linkToSingleResource(PukDto.class, pukDto.getPukId());
    Resource<DomainObjectDTO> pukResource = new Resource<>(pukDto, linkForPuk);
    return new ResponseEntity<>(pukResource, HttpStatus.OK);
  }


  private Resource<PukDto> mappedPuk(Puk puk) {
    long pukId = puk.getPukId();
    Link selfLink = linkTo(methodOn(PukController.class).getPukById(pukId)).withSelfRel();
    PukDto pukDto = modelMapper.map(puk, PukDto.class);
    Link pukItemsLink = linkTo(methodOn(PukItemController.class).retrievePukItemForPuk(pukId, new PageRequest(0, 20))).withRel("puk-item");
    Link pukGroupLink = linkTo(methodOn(PukGroupController.class).getPukGroupById(puk.getPukGroup().getPukGroupId())).withRel("puk-group");
    Link allPukGroups = linkTo(methodOn(PukGroupController.class).getAllPukGroups(new PageRequest(0, 20))).withRel("all-puk-group");
    Link associatedPces = linkTo(methodOn(PceController.class).getPceByPukId(pukId, new PageRequest(0, 20))).withRel("associated-pce");
    Link allPces = linkTo(methodOn(PceController.class).getPce(new PageRequest(0, 20))).withRel("all-pce");

    return new Resource<>(pukDto, selfLink, pukItemsLink, pukGroupLink, allPukGroups, associatedPces, allPces);
  }


}
