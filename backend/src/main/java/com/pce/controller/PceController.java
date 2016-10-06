package com.pce.controller;

import com.pce.domain.Pce;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PceDto;
import com.pce.domain.dto.PukDto;
import com.pce.domain.dto.RecipientBankAccountDto;
import com.pce.service.PceService;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.PceCreateValidator;
import com.pce.validation.validator.PceUpdateValidator;
import com.pce.validation.validator.ValidationErrorBuilder;
import org.modelmapper.ModelMapper;
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
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

  @Autowired
  private PagedResourcesAssembler assembler;

  public static final String PCE_URL_PATH = "/pce";

  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<PceDto>> getPceById(@PathVariable Long id) {
    Pce pce = pceService.getPceByPceId(id).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", id)));
    return new ResponseEntity<>(mappedPce(pce), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPce(Pageable pageRequest) {
    Page<Pce> allPces = pceService.getAllAvailablePce(pageRequest);
    Page<Resource<PceDto>> newPaged = allPces.map(source -> mappedPce(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @RequestMapping(value = "/bypuk/{pukId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPceByPukId(@PathVariable("pukId") long pukId, Pageable pageRequest) {
    Page<Pce> allPces = pceService.getAvailablePceByPukId(pukId, pageRequest);
    if (CollectionUtils.isEmpty(allPces.getContent())) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    Page<Resource<PceDto>> newPaged = allPces.map(source -> mappedPce(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @PreAuthorize("@pceUserServiceImpl.canCurrentUserCreateOrUpdatePce(principal)")
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


  @PreAuthorize("@pceUserServiceImpl.canCurrentUserCreateOrUpdatePce(principal)")
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

  private Resource<PceDto> mappedPce(Pce pce) {
    long pceId = pce.getPceId();
    Link selfLink = linkTo(methodOn(PceController.class).getPceById(pceId)).withSelfRel();
    PceDto pceDto = modelMapper.map(pce, PceDto.class);
    Link pukLink = linkTo(methodOn(PukController.class).getPukByPceId(pceId)).withRel("puk");
    Link allPuk = entityLinks.linkToCollectionResource(PukDto.class).withRel("all-puks");
    Link pceItems = linkTo(methodOn(PceItemController.class).getPceItemsByPceId(pceId, new PageRequest(0, 20))).withRel("pce-item");
    Link recipient = linkTo(methodOn(RecipientBankAcctController.class).getRecipientByPceId(pceId)).withRel("recipient");
    Link allRecipient = entityLinks.linkToCollectionResource(RecipientBankAccountDto.class).withRel("all-recipient");
    Link approvers = linkTo(methodOn(UserController.class).getApproversByPceId(pceId)).withRel("current-approvers");
    return new Resource<>(pceDto, selfLink, pukLink, allPuk, pceItems, recipient, allRecipient, approvers);
  }


}
