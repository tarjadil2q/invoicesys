package com.pce.controller;

import com.pce.domain.Puk;
import com.pce.domain.PukItem;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukItemDto;
import com.pce.service.PukService;
import com.pce.service.mapper.PukItemMapper;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.PukItemValidator;
import com.pce.validation.validator.ValidationErrorBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 6/09/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/puk")
@ExposesResourceFor(PukItemDto.class)
public class PukItemController {

  private PukService pukService;
  private PukItemMapper pukItemMapper;
  private PukItemValidator pukItemValidator;
  @Autowired
  private PagedResourcesAssembler assembler;

  @Autowired
  private ModelMapper modelMapper;


  private static final String PUK_ITEM_URL_PATH = "/pukitem";

  @Autowired
  public PukItemController(PukService pukService, PukItemMapper pukItemMapper,
                           PukItemValidator pukItemValidator) {
    this.pukService = pukService;
    this.pukItemMapper = pukItemMapper;
    this.pukItemValidator = pukItemValidator;
  }

  @PreAuthorize("@pukUserServiceImpl.canCurrentUserCreateOrUpdatePuk(principal)")
  @RequestMapping(value = "/{pukId}/pukitem/{pukItemId}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePukItemForParticularPuk(@PathVariable("pukId") long pukId,
                                                                             @PathVariable("pukItemId") long pukItemId,
                                                                             @RequestBody @Valid PukItemDto pukItemDto, Errors errors) {


    Puk puk = pukService.getPukByPukId(pukId).orElseThrow(() -> new NoSuchElementException(String.format("Puk=%s not found", pukId)));
    PukItem pukItem = pukService.getPukItemByPukItemId(pukItemId).orElseThrow(() -> new NoSuchElementException(String.format("PukItem=%s not found", pukItemId)));
    ValidationUtils.invokeValidator(pukItemValidator, pukItemDto, errors);
    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    PukItem mappedPukItem = pukItemMapper.mapDtoIntoEntity(pukItemDto);
    mappedPukItem.setPukItemId(pukItem.getPukItemId());
    pukService.createOrUpdatePukItem(puk, mappedPukItem);
    pukItemDto.add(ControllerLinkBuilder.linkTo(PukItemController.class).slash(pukId).slash(PUK_ITEM_URL_PATH).slash(pukItemId).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukItemDto, HttpStatus.OK);

  }

  @PreAuthorize("@pukUserServiceImpl.canCurrentUserCreateOrUpdatePuk(principal)")
  @RequestMapping(value = "/{pukId}/pukitem", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> createPukItemForParticularPuk(@PathVariable("pukId") long pukId,
                                                                             @RequestBody @Valid PukItemDto pukItemDto, Errors errors) {
    Puk puk = pukService.getPukByPukId(pukId).orElseThrow(() -> new NoSuchElementException(String.format("Puk=%s not found", pukId)));

    ValidationUtils.invokeValidator(pukItemValidator, pukItemDto, errors);
    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    PukItem mappedPukItem = pukItemMapper.mapDtoIntoEntity(pukItemDto);
    PukItem updatedPukItem = pukService.createOrUpdatePukItem(puk, mappedPukItem);


    pukItemDto.add(ControllerLinkBuilder.linkTo(PukItemController.class).slash(pukId).slash(PUK_ITEM_URL_PATH).slash(updatedPukItem.getPukItemId()).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukItemDto, HttpStatus.CREATED);

  }

  @RequestMapping(value = "/{pukId}/pukitem", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> retrievePukItemForPuk(@PathVariable("pukId") long pukId,
                                                                           Pageable pageRequest) {
    Page<PukItem> allPukItems = pukService.getPukItemsByPukId(pukId, pageRequest);
    Page<Resource<PukItemDto>> newPaged = allPukItems.map(source -> mappedPukItem(pukId, source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }


  @RequestMapping(value = "/{pukId}/pukitem/{pukItemId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> retrievePukItemForPuk(@PathVariable("pukId") long pukId,
                                                                     @PathVariable("pukItemId") long pukItemId) {
    PukItem pukItem = pukService.getPukItemByPukIdAndPukItemId(pukId, pukItemId).orElseThrow(() -> new NoSuchElementException(String.format("PukItem=%s not found", pukItemId)));
    PukItemDto pukItemDto = (PukItemDto) pukItemMapper.mapEntityIntoDto(pukItem);
    pukItemDto.add(ControllerLinkBuilder.linkTo(PukItemController.class).slash(pukId).slash(PUK_ITEM_URL_PATH).slash(pukItemId).withSelfRel());

    return ControllerHelper.getResponseEntity(pukItemDto);
  }

  private Resource<PukItemDto> mappedPukItem(long pukId, PukItem pukItem) {
    Link selfLink = linkTo(methodOn(PukItemController.class).retrievePukItemForPuk(pukId, pukItem.getPukItemId())).withSelfRel();
    Link pukItemMeasurementLink = linkTo(methodOn(PukItemMeasurementController.class).getPukItemMeasurementById(pukItem.getPukItemMeasurement().getPukItemMeasurementId())).withRel("pukitem-measurement");
    Link allPukItemMeasurements = linkTo(methodOn(PukItemMeasurementController.class).getPukItemMeasurements(new PageRequest(0, 20))).withRel("all-pukitem-measurement");
    PukItemDto pukItemDto = modelMapper.map(pukItem, PukItemDto.class);
    return new Resource<>(pukItemDto, selfLink, pukItemMeasurementLink, allPukItemMeasurements);
  }


}
