package com.pce.controller;

import com.pce.domain.*;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PceApprovalWrapperDto;
import com.pce.domain.dto.PceDto;
import com.pce.service.EmailService;
import com.pce.service.PceApprovalRoleService;
import com.pce.service.PceService;
import com.pce.service.UserService;
import com.pce.service.mapper.PceMapper;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.PceApproveValidator;
import com.pce.validation.validator.PceRejectValidator;
import com.pce.validation.validator.ValidationErrorBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 16/09/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/approval")
@ExposesResourceFor(PceDto.class)
public class PceApprovalController {
  @Autowired
  private PceApprovalRoleService pceApprovalRoleService;
  @Autowired
  private PceService pceService;
  @Autowired
  private PceApproveValidator pceApproveValidator;

  @Autowired
  private PceRejectValidator pceRejectValidator;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PceMapper pceMapper;

  @Autowired
  private UserService userService;

  @Autowired
  private PagedResourcesAssembler assembler;

  @Autowired
  private EmailService mailService;


  private static final String PCE_APPROVAL_URL_PATH = "/pce";


  @RequestMapping(value = "/currentuser", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<Resource<PceDto>>> getAllPceToBeApproveOrRejectByCurrentUser(Authentication authentication, Pageable pageRequest) {
    CurrentUser principal = (CurrentUser) authentication.getPrincipal();
    Page<Pce> allPceToBeApproved = pceService.findAllPceToBeApproved(principal, pageRequest);
    if (allPceToBeApproved == null || !allPceToBeApproved.hasContent()) {
      ArrayList<Pce> emptyPces = new ArrayList<>();
      return new ResponseEntity<>(assembler.toResource(new PageImpl(emptyPces)), HttpStatus.OK);
    }
    Page<Resource<PceDto>> newPaged = allPceToBeApproved.map(source -> pceMapper.mappedPce(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.canCurrentUserApproveOrRejectPce(principal)")
  @RequestMapping(value = "/approve/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> approvePce(@PathVariable("id") long id,
                                                          Authentication authentication) {
    Pce pce = pceService.getPceByPceId(id).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", id)));
    CurrentUser principal = (CurrentUser) authentication.getPrincipal();
    Set<User> pukGroupUsers = pce.getAssociatedPuk().getPukGroup().getPukGroupUsers();
    PceApprovalWrapperDto pceApprovalWrapperDto = new PceApprovalWrapperDto(principal,
            pukGroupUsers, pce);
    BindException bindException = new BindException(pceApprovalWrapperDto, "pceApprovalWrapperDto");
    ValidationUtils.invokeValidator(pceApproveValidator, pceApprovalWrapperDto, bindException);

    if (bindException.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(bindException);
    }

    if (pceService.approvePce(pce, principal)) {
      Optional<User> userByRoleOptional = getNextApprover(pce);
      if (userByRoleOptional.isPresent()) {
        mailService.sendEmail(userByRoleOptional.get().getEmail(), "GKY Sydney PCE waiting for approval", "There is PCE waiting for your approval. " +
                "Please go to this " + "<a href=" + linkTo(PceApprovalController.class).slash("/currentuser").toString() + ">" + "link" + "</a>");
      }
      PceDto pceDto = modelMapper.map(pce, PceDto.class);
      pceDto.add(linkTo(UserController.class).slash(PCE_APPROVAL_URL_PATH).slash(pce.getPceId()).withSelfRel());

      return ControllerHelper.getResponseEntityWithoutBody(pceDto, HttpStatus.OK);
    }

    bindException.reject("userRole.Invalid", "This current user " + principal.getUser().getFirstName() + " " +
            principal.getUser().getLastName() + " is not in the right approval role sequence");
    return ValidationErrorBuilder.fromBindingErrors(bindException);
  }

  private Optional<User> getNextApprover(Pce pce) {
    List<PceApprovalRole> validApprovalRoles = pceApprovalRoleService.findAllAvailableApprovalRoleOrderBySequenceNoAsc();
    List<Role> approvalRoles = validApprovalRoles.stream().map(pceRole -> pceRole.getPceApprovalRole()).collect(Collectors.toList());
    Role nextApproverOrRejecterRole = pceService.getNextApproverOrRejecterRole(pce.getApprovers(), approvalRoles);
    return userService.getUserByRole(nextApproverOrRejecterRole);
  }

  @PreAuthorize("@currentUserServiceImpl.canCurrentUserApproveOrRejectPce(principal)")
  @RequestMapping(value = "/reject/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> rejectPce(@PathVariable("id") long id,
                                                         Authentication authentication) {
    Pce pce = pceService.getPceByPceId(id).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", id)));
    CurrentUser principal = (CurrentUser) authentication.getPrincipal();
    Set<User> pukGroupUsers = pce.getAssociatedPuk().getPukGroup().getPukGroupUsers();
    PceApprovalWrapperDto pceApprovalWrapperDto = new PceApprovalWrapperDto(principal,
            pukGroupUsers, pce);

    BindException bindException = new BindException(pceApprovalWrapperDto, "pceApprovalWrapperDto");
    ValidationUtils.invokeValidator(pceRejectValidator, pceApprovalWrapperDto, bindException);

    if (bindException.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(bindException);
    }

    if (pceService.rejectPce(pce, principal)) {
      List<PceApprovalRole> validApprovalRoles = pceApprovalRoleService.findAllAvailableApprovalRoleOrderBySequenceNoAsc();
      List<Role> approvalRoles = validApprovalRoles.stream().map(pceRole -> pceRole.getPceApprovalRole()).collect(Collectors.toList());
      if (!CollectionUtils.isEmpty(approvalRoles)) {
        Role rejecterRole = approvalRoles.get(0);
        Optional<User> rejectedUserByRole = userService.getUserByRole(rejecterRole);
        if (rejectedUserByRole.isPresent()) {
          mailService.sendEmail(rejectedUserByRole.get().getEmail(), "GKY Sydney PCE rejected.",
                  "There is PCE being rejected." +
                          " Please go to this <a href=" + linkTo(methodOn(PceController.class).getPceById(pce.getPceId())).toString() + ">" + "link</a>" + " for more detail.");
        }

      }
      PceDto pceDto = modelMapper.map(pce, PceDto.class);
      pceDto.add(linkTo(PceApprovalController.class).slash(pce.getPceId()).withRel(PCE_APPROVAL_URL_PATH).withSelfRel());

      return ControllerHelper.getResponseEntityWithoutBody(pceDto, HttpStatus.OK);
    }

    bindException.reject("userRole.Invalid", "This current user " + principal.getUser().getFirstName() + " " +
            principal.getUser().getLastName() + " is not in the right approval role sequence");
    return ValidationErrorBuilder.fromBindingErrors(bindException);

  }


}
