package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.Pce;
import com.pce.domain.RecipientBankAccount;
import com.pce.domain.dto.ApiError;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.RecipientBankAccountDto;
import com.pce.service.RecipientBankAcctService;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.RecipientAccountCreateValidator;
import com.pce.validation.validator.RecipientAccountUpdateValidator;
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
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/recipient")
@ExposesResourceFor(RecipientBankAccountDto.class)
public class RecipientBankAcctController {

  public static final String RECIPIENT_BANK_ACCOUNT_URL_PATH = "/recipient";

  @Autowired
  private RecipientBankAcctService recipientBankAcctService;

  @Autowired
  private ModelMapper modelMapper;


  @Autowired
  private EntityLinks entityLinks;

  @Autowired
  private RecipientAccountCreateValidator recipientAccountCreateValidator;

  @Autowired
  private RecipientAccountUpdateValidator recipientAccountUpdateValidator;

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{recipientId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getRecipientBankAccountById(@PathVariable("recipientId") long recipientId) {
    RecipientBankAccount recipientBankAccount = recipientBankAcctService.findRecipientBankAccountById(recipientId).orElseThrow(() -> new NoSuchElementException(String.format("Recipient bank account=%s not found", recipientId)));
    RecipientBankAccountDto recipientBankAccountDto = modelMapper.map(recipientBankAccount, RecipientBankAccountDto.class);
    Link linkForRecipientBankDto = entityLinks.linkToSingleResource(RecipientBankAccountDto.class, recipientBankAccountDto.getRecipientBankAccountId());
    Resource<DomainObjectDTO> pukResource = new Resource<>(recipientBankAccountDto, linkForRecipientBankDto);
    return new ResponseEntity<>(pukResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getRecipients(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<RecipientBankAccount> pageRecipient = recipientBankAcctService.findAllRecipientBankAccount(pageRequest);

    Page<RecipientBankAccountDto> newPaged = pageRecipient.map(source -> modelMapper.map(source, RecipientBankAccountDto.class));

    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createRecipient(@RequestBody @Valid RecipientBankAccountDto recipientBankAccountDto, Errors errors) {
    ValidationUtils.invokeValidator(recipientAccountCreateValidator, recipientBankAccountDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    RecipientBankAccount bankAccount = modelMapper.map(recipientBankAccountDto, RecipientBankAccount.class);
    Optional<RecipientBankAccount> recipientBankAccount = recipientBankAcctService.findRecipientBankAccountByAccountNumberAndBsb(recipientBankAccountDto.getAcctNumber(),
            recipientBankAccountDto.getBsb());
    if (recipientBankAccount.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.CONFLICT,
              "Recipient bank account already exists, please enter different bank account", Lists.newArrayList("Bank account already exists"))), HttpStatus.CONFLICT);
    }

    RecipientBankAccount bankAcount = recipientBankAcctService.createOrUpdateRecipientBankAccount(bankAccount);
    recipientBankAccountDto.add(ControllerLinkBuilder.linkTo(RecipientBankAcctController.class).slash(bankAcount.getRecipientBankAccountId()).withRel(RECIPIENT_BANK_ACCOUNT_URL_PATH).withSelfRel());
    return ControllerHelper.getResponseEntityWithoutBody(recipientBankAccountDto, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updateBankAccount(@PathVariable("id") long id,
                                                                 @RequestBody @Valid RecipientBankAccountDto recipientBankAccountDto,
                                                                 Errors errors) {
    Optional<RecipientBankAccount> currentBankAccount = recipientBankAcctService.findRecipientBankAccountById(id);

    if (!currentBankAccount.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Bank account to be updated not found, please check id is correct ", "Bank Account id is not found")), HttpStatus.NOT_FOUND);
    }
    recipientBankAccountDto.setRecipientBankAccountId(id);
    ValidationUtils.invokeValidator(recipientAccountUpdateValidator, recipientBankAccountDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    RecipientBankAccount bankAccount = modelMapper.map(recipientBankAccountDto, RecipientBankAccount.class);


    RecipientBankAccount updatedBankAccount = recipientBankAcctService.createOrUpdateRecipientBankAccount(bankAccount);
    recipientBankAccountDto.add(ControllerLinkBuilder.linkTo(PukItemMeasurementController.class).slash(updatedBankAccount.getRecipientBankAccountId()).withRel(RECIPIENT_BANK_ACCOUNT_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(recipientBankAccountDto, HttpStatus.OK);
  }

  @RequestMapping(value = "/pce/{pceId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<RecipientBankAccountDto>> getRecipientByPceId(@PathVariable("pceId") long pceId) {
    Pce pce = new Pce();
    pce.setPceId(pceId);
    Optional<RecipientBankAccount> recipientBankAccount = recipientBankAcctService.findRecipientBankAccountByPce(pce);
    if (!recipientBankAccount.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(mappedRecipient(recipientBankAccount.get()), HttpStatus.OK);
  }

  private Resource<RecipientBankAccountDto> mappedRecipient(RecipientBankAccount recipient) {
    Link selfLink = linkTo(methodOn(RecipientBankAcctController.class).getRecipientBankAccountById(recipient.getRecipientBankAccountId())).withSelfRel();
    RecipientBankAccountDto recipientDto = modelMapper.map(recipient, RecipientBankAccountDto.class);
    return new Resource<>(recipientDto, selfLink);
  }

}
