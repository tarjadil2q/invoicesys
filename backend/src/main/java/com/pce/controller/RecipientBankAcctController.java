package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.RecipientBankAccount;
import com.pce.domain.dto.*;
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

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
@RestController
@RequestMapping("/api/v1/recipient")
@ExposesResourceFor(PceItemDto.class)
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
  @RequestMapping(value = "/{recipientId}/pukitem/{pukItemId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getRecipientBankAccountById(@PathVariable("recipientId") long recipientId) {
    RecipientBankAccount recipientBankAccount = recipientBankAcctService.findRecipientBankAccountById(recipientId).orElseThrow(() -> new NoSuchElementException(String.format("Recipient bank account=%s not found", recipientId)));
    RecipientBankAcctDto recipientBankAcctDto = modelMapper.map(recipientBankAccount, RecipientBankAcctDto.class);
    Link linkForRecipientBankDto = entityLinks.linkToSingleResource(PukDto.class, recipientBankAcctDto.getRecipientBankAcctId());
    Resource<DomainObjectDTO> pukResource = new Resource<>(recipientBankAcctDto, linkForRecipientBankDto);
    return new ResponseEntity<>(pukResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getRecipients(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<RecipientBankAccount> pageRecipient = recipientBankAcctService.findAllRecipientBankAccount(pageRequest);

    Page<RecipientBankAcctDto> newPaged = pageRecipient.map(source -> modelMapper.map(source, RecipientBankAcctDto.class));

    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createRecipient(@RequestBody @Valid RecipientBankAcctDto recipientBankAcctDto, Errors errors) {
    ValidationUtils.invokeValidator(recipientAccountCreateValidator, recipientBankAcctDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    RecipientBankAccount bankAccount = modelMapper.map(recipientBankAcctDto, RecipientBankAccount.class);
    Optional<RecipientBankAccount> recipientBankAccount = recipientBankAcctService.findRecipientBankAccountByAccountNumberAndBsb(recipientBankAcctDto.getAcctNumber(),
            recipientBankAcctDto.getBsb());
    if (recipientBankAccount.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.CONFLICT,
              "Recipient bank account already exists, please enter different bank account", Lists.newArrayList("Bank account already exists"))), HttpStatus.CONFLICT);
    }

    RecipientBankAccount bankAcount = recipientBankAcctService.createOrUpdateRecipientBankAccount(bankAccount);
    recipientBankAcctDto.add(ControllerLinkBuilder.linkTo(PukGroupController.class).slash(bankAcount.getRecipientBankAccountId()).withRel(RECIPIENT_BANK_ACCOUNT_URL_PATH).withSelfRel());
    return ControllerHelper.getResponseEntityWithoutBody(recipientBankAcctDto, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updateBankAccount(@PathVariable("id") long id,
                                                                 @RequestBody @Valid RecipientBankAcctDto recipientBankAcctDto,
                                                                 Errors errors) {
    Optional<RecipientBankAccount> currentBankAccount = recipientBankAcctService.findRecipientBankAccountById(id);

    if (!currentBankAccount.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Bank account to be updated not found, please check id is correct ", "Bank Account id is not found")), HttpStatus.NOT_FOUND);
    }
    recipientBankAcctDto.setRecipientBankAcctId(id);
    ValidationUtils.invokeValidator(recipientAccountUpdateValidator, recipientBankAcctDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    RecipientBankAccount bankAccount = modelMapper.map(recipientBankAcctDto, RecipientBankAccount.class);


    RecipientBankAccount updatedBankAccount = recipientBankAcctService.createOrUpdateRecipientBankAccount(bankAccount);
    recipientBankAcctDto.add(ControllerLinkBuilder.linkTo(PukItemMeasurementController.class).slash(updatedBankAccount.getRecipientBankAccountId()).withRel(RECIPIENT_BANK_ACCOUNT_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(recipientBankAcctDto, HttpStatus.OK);
  }


}
