package com.pce.controller;

import com.pce.domain.Pce;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PceDto;
import com.pce.domain.dto.PukDto;
import com.pce.service.PceService;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.PceCreateValidator;
import com.pce.validation.validator.PceUpdateValidator;
import com.pce.validation.validator.ValidationErrorBuilder;
import org.modelmapper.ModelMapper;
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
 * Created by Leonardo Tarjadi on 5/09/2016.
 */
@RestController
@RequestMapping("/api/v1/pce")
@ExposesResourceFor(PceDto.class)
public class PceController {
  @Autowired
  private PceService pceService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private EntityLinks entityLinks;

  @Autowired
  private PceCreateValidator pceCreateValidator;

  @Autowired
  private PceUpdateValidator pceUpdateValidator;

  public static final String PCE_URL_PATH = "/pce";

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPceById(@PathVariable Long id) {
    Pce pce = pceService.getPceByPceId(id).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", id)));
    PceDto pceDto = modelMapper.map(pce, PceDto.class);
    Link linkForPuk = entityLinks.linkToSingleResource(PukDto.class, pceDto.getPceId());
    Resource<DomainObjectDTO> pukResource = new Resource<>(pceDto, linkForPuk);
    return new ResponseEntity<>(pukResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPce(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<Pce> allPces = pceService.getAllAvailablePce(pageRequest);
    Page<PceDto> newPaged = allPces.map(source -> modelMapper.map(source, PceDto.class));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createPce(@RequestBody @Valid PceDto pceDto, Errors errors) {
    ValidationUtils.invokeValidator(pceCreateValidator, pceDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }
    Pce pce = modelMapper.map(pceDto, Pce.class);

    Pce createdPce = pceService.createOrUpdatePce(pce);
    pceDto.add(ControllerLinkBuilder.linkTo(PceController.class).slash(createdPce.getPceId()).withRel(PCE_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pceDto, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePce(@PathVariable("id") long id,
                                                         @RequestBody @Valid PceDto pceDto, Errors errors) {

    Pce pce = pceService.getPceByPceId(id).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", id)));
    pceDto.setPceId(id);
    ValidationUtils.invokeValidator(pceUpdateValidator, pceDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    Pce mappedPce = modelMapper.map(pceDto, Pce.class);
    mappedPce.setPceId(pce.getPceId());
    Pce updatedPce = pceService.createOrUpdatePce(mappedPce);

    pceDto.add(ControllerLinkBuilder.linkTo(PukController.class).slash(updatedPce.getPceId()).withRel(PCE_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pceDto, HttpStatus.OK);

  }

}
