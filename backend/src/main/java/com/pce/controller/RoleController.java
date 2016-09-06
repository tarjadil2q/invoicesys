package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.Role;
import com.pce.domain.dto.ApiError;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.RoleDto;
import com.pce.service.RoleService;
import com.pce.service.mapper.RoleMapper;
import com.pce.util.ControllerHelper;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 11/02/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/role")
@ExposesResourceFor(RoleDto.class)
public class RoleController {

  private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

  public static final String ROLE_URL_PATH = "/role";

  private RoleService roleService;
  private RoleMapper roleMapper;
  private EntityLinks entityLinks;

  @Autowired
  public RoleController(RoleService roleService, RoleMapper roleMapper, EntityLinks entityLinks) {
    this.roleService = roleService;
    this.roleMapper = roleMapper;
    this.entityLinks = entityLinks;
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getRoleById(@PathVariable Long id) {
    Role role = roleService.getRoleById(id).orElseThrow(() -> new NoSuchElementException(String.format("Role=%s not found", id)));
    RoleDto roleDto = (RoleDto) roleMapper.mapEntityIntoDto(role);
    Link linkForRole = entityLinks.linkToSingleResource(RoleDto.class, roleDto.getRoleId());
    Resource<DomainObjectDTO> userResource = new Resource<>(roleDto, linkForRole);
    return new ResponseEntity<>(userResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getRoles(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<Role> allRoles = roleService.getAllAvailableRoles(pageRequest);
    Page<DomainObjectDTO> roleDtos = roleMapper.mapEntityPageIntoDTOPage(pageRequest, allRoles);
    return new ResponseEntity<>(assembler.toResource(roleDtos), HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createRole(@RequestBody @Valid RoleDto roleDto) {

    Role role = roleMapper.mapDtoIntoEntity(roleDto);
    List<Role> rolesFound = roleService.findRoleByRoleNameIgnoreCase(roleDto.getRoleName());
    if (!CollectionUtils.isEmpty(rolesFound)) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.CONFLICT,
              "Role already exists, please enter different Role", Lists.newArrayList("Role already exists"))), HttpStatus.CONFLICT);
    }

    roleService.createOrUpdateRole(role);
    roleDto.add(ControllerLinkBuilder.linkTo(RoleController.class).slash(role.getId()).withRel(ROLE_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(roleDto, HttpStatus.CREATED);

  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updateUser(@PathVariable("id") long id,
                                                          @RequestBody @Valid RoleDto roleDto) {
    Optional<Role> currentRole = roleService.getRoleById(id);

    if (!currentRole.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Role to be updated not found, please check id is correct ", "Role id is not found")), HttpStatus.NOT_FOUND);
    }

    Role roleToBeUpdate = currentRole.get();
    roleToBeUpdate.setRoleName(roleDto.getRoleName());

    Role updatedRole = roleService.createOrUpdateRole(roleToBeUpdate);
    roleDto.add(ControllerLinkBuilder.linkTo(RoleController.class).slash(updatedRole.getId()).withRel(ROLE_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(roleDto, HttpStatus.OK);
  }
}
