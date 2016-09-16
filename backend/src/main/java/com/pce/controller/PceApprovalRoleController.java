package com.pce.controller;

import com.pce.domain.PceApprovalRole;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PceApprovalRoleDto;
import com.pce.service.PceApprovalRoleService;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.PceApprovalRoleCreateValidator;
import com.pce.validation.validator.PceApprovalRoleUpdateValidator;
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
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/pceapproval")
@ExposesResourceFor(PceApprovalRoleDto.class)
public class PceApprovalRoleController {

  public static final String PCE_APPROVAL_ROLE_PATH = "/pceapproval";

  @Autowired
  private PceApprovalRoleService pceApprovalRoleService;

  @Autowired
  private PceApprovalRoleCreateValidator pceApprovalRoleCreateValidator;

  @Autowired
  private PceApprovalRoleUpdateValidator pceApprovalRoleUpdateValidator;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private EntityLinks entityLinks;

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createPceApprovalRole(@RequestBody @Valid PceApprovalRoleDto pceApprovalRoleDto,
                                                                     Errors errors) {

    ValidationUtils.invokeValidator(pceApprovalRoleCreateValidator, pceApprovalRoleDto, errors);

    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    PceApprovalRole pceApprovalRole = modelMapper.map(pceApprovalRoleDto, PceApprovalRole.class);
    pceApprovalRole.setRoleId(0);
    pceApprovalRoleService.createOrUpdatePceApprovalRole(pceApprovalRole);
    pceApprovalRoleDto.add(ControllerLinkBuilder.linkTo(PceApprovalRoleController.class).slash(pceApprovalRole.getRoleId()).withRel(PCE_APPROVAL_ROLE_PATH).withSelfRel());
    return ControllerHelper.getResponseEntityWithoutBody(pceApprovalRoleDto, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPceApprovalRoleById(@PathVariable Long id) {
    PceApprovalRole pceApprovalRole = pceApprovalRoleService.findPceApprovalRoleById(id).orElseThrow(() -> new NoSuchElementException(String.format("Pce Approval role=%s not found", id)));
    PceApprovalRoleDto pceApprovalRoleDto = modelMapper.map(pceApprovalRole, PceApprovalRoleDto.class);
    Link linkForResource = entityLinks.linkToSingleResource(PceApprovalRoleDto.class, pceApprovalRoleDto.getRoleId());
    Resource<DomainObjectDTO> userResource = new Resource<>(pceApprovalRoleDto, linkForResource);
    return new ResponseEntity<>(userResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPceApprovalRoles(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<PceApprovalRole> allPceApprovalRoles = pceApprovalRoleService.findAllAvailableApprovalRole(pageRequest);
    Page<DomainObjectDTO> pceApprovalRoles = allPceApprovalRoles.map(source -> modelMapper.map(source, PceApprovalRoleDto.class));
    return new ResponseEntity<>(assembler.toResource(pceApprovalRoles), HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePceApprovalRole(@PathVariable("id") long id,
                                                                     @RequestBody @Valid PceApprovalRoleDto pceApprovalRoleDto,
                                                                     Errors errors) {
    if (errors.hasErrors()) {
      return ValidationErrorBuilder.fromBindingErrors(errors);
    }

    PceApprovalRole existingPceApprovalRole = pceApprovalRoleService.findPceApprovalRoleById(id).orElseThrow(() -> new NoSuchElementException(String.format("Pce Approval role=%s not found", id)));

    PceApprovalRole pceApprovalRole = modelMapper.map(pceApprovalRoleDto, PceApprovalRole.class);
    pceApprovalRole.setRoleId(existingPceApprovalRole.getRoleId());
    PceApprovalRole updatedPceApprovalRole = pceApprovalRoleService.createOrUpdatePceApprovalRole(pceApprovalRole);
    pceApprovalRoleDto.add(ControllerLinkBuilder.linkTo(PceApprovalRoleController.class).slash(updatedPceApprovalRole.getRoleId()).withRel(PCE_APPROVAL_ROLE_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pceApprovalRoleDto, HttpStatus.OK);
  }


}
