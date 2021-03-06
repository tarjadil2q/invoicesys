package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.*;
import com.pce.domain.dto.*;
import com.pce.service.CurrentUserService;
import com.pce.service.PukGroupService;
import com.pce.service.RoleService;
import com.pce.service.UserService;
import com.pce.service.mapper.UserMapper;
import com.pce.util.ControllerHelper;
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
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 7/02/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/user")
@ExposesResourceFor(UserDto.class)

public class UserController {

  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  public static final String USER_REQUEST_PATH = "/user";

  private UserService userService;
  private RoleService roleService;
  private UserMapper userMapper;
  private EntityLinks entityLinks;
  private CurrentUserService currentUserService;
  private PukGroupService pukGroupService;
  private PagedResourcesAssembler assembler;
  private ModelMapper modelMapper;

  @Autowired
  public UserController(UserService userService,
                        RoleService roleService,
                        UserMapper userMapper,
                        EntityLinks entityLinks,
                        CurrentUserService currentUserService,
                        PukGroupService pukGroupService,
                        PagedResourcesAssembler assembler,
                        ModelMapper modelMapper) {
    this.userService = userService;
    this.roleService = roleService;
    this.userMapper = userMapper;
    this.entityLinks = entityLinks;
    this.currentUserService = currentUserService;
    this.pukGroupService = pukGroupService;
    this.assembler = assembler;
    this.modelMapper = modelMapper;
  }


  @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<UserDto>> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id).orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", id)));

    return new ResponseEntity<>(mappedUser(user), HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")

  public HttpEntity<PagedResources<DomainObjectDTO>> getUsers(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<User> allUsers = userService.getAllUsers(pageRequest);
    if (CollectionUtils.isEmpty(allUsers.getContent())) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    Page<Resource<UserDto>> newPaged = allUsers.map(source -> mappedUser(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createUser(@RequestBody @Valid UserDto userDto) {

    User user = userMapper.mapDtoIntoEntity(userDto);
    if (userService.isUserExists(userDto.getEmail())) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.CONFLICT,
              "User already exists, please enter different user", Lists.newArrayList("User already exists"))), HttpStatus.CONFLICT);
    }
    Set<Role> roles = getRoles(userDto);
    if (CollectionUtils.isEmpty(roles)) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.BAD_REQUEST,
              "No Roles being defined for user " + user.getEmail(), "No Roles being defined")), HttpStatus.BAD_REQUEST);
    }
    user = userService.createOrUpdate(user, roles);
    userDto.add(ControllerLinkBuilder.linkTo(UserController.class).slash(user.getId()).withRel(USER_REQUEST_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(userDto, HttpStatus.CREATED);

  }


  @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updateUser(@PathVariable("id") long id,
                                                          @RequestBody @Valid UserDto userDto,
                                                          Authentication authentication) {
    Optional<User> currentUser = userService.getUserById(id);

    if (!currentUser.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "User to be updated not found, please check id is correct ", "User id is not found")), HttpStatus.NOT_FOUND);
    }
    CurrentUser currentUserPrincipal = (CurrentUser) authentication.getPrincipal();


    User userToBeUpdate = currentUser.get();
    userToBeUpdate.setFirstName(userDto.getFirstName());
    userToBeUpdate.setFirstName(userDto.getLastName());
    userToBeUpdate.setFirstName(userDto.getEmail());

    User updatedUser = getUpdatedUser(currentUserPrincipal, userDto, userToBeUpdate);

    userDto.setLink(updatedUser.getId(), USER_REQUEST_PATH);
    return ControllerHelper.getResponseEntityWithoutBody(userDto, HttpStatus.OK);

  }

  @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
  @RequestMapping(value = "/{userId}/pukgroup/{pukGroupId}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> addUserToPukGroup(@PathVariable("userId") long userId,
                                                                 @PathVariable("pukGroupId") long pukGroupId) {
    User user = userService.getUserById(userId).orElseThrow(() -> new NoSuchElementException(String.format("UserId=%s not found", userId)));
    PukGroup pukGroup = pukGroupService.getPukGroupById(pukGroupId).orElseThrow(() -> new NoSuchElementException(String.format("PukGroupId=%s not found", pukGroupId)));
    userService.addUserToPukGroup(user, pukGroup);
    UserDto userDto = (UserDto) userMapper.mapEntityIntoDto(user);
    userDto.setLink(user.getId(), USER_REQUEST_PATH);
    return ControllerHelper.getResponseEntityWithoutBody(userDto, HttpStatus.OK);
  }

  @RequestMapping(value = "/pukgroup/{pukGroupId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getUsersByPukGroup(@PathVariable("pukGroupId") long pukGroupId,
                                                                        Pageable pageRequest) {
    PukGroup pukGroup = new PukGroup();
    pukGroup.setPukGroupId(pukGroupId);
    Page<User> allUsers = userService.getUsersForPukGroup(pageRequest, pukGroup);
    Page<Resource<UserDto>> newPaged = allUsers.map(source -> mappedUser(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }


  @RequestMapping(value = "/pce/{pceId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resources<DomainObjectDTO>> getApproversByPceId(@PathVariable("pceId") long id) {
    Pce pce = new Pce();
    pce.setPceId(id);
    List<User> approvers = userService.getApproversByPce(pce);
    if (CollectionUtils.isEmpty(approvers)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    List<Resource<UserDto>> userDtos = approvers.stream().map(user -> mappedUser(user)).collect(Collectors.toList());
    Link selfLink = linkTo(methodOn(UserController.class).getApproversByPceId(id)).withSelfRel();
    Resources resources = new Resources(userDtos, selfLink);

    return new ResponseEntity<>(resources, HttpStatus.OK);
  }


  private User getUpdatedUser(CurrentUser currentUserPrincipal, UserDto userDto,
                              User userToBeUpdate) {
    if (currentUserService.isCurrentUserAdmin(currentUserPrincipal)) {
      Set<Role> roles = getRoles(userDto);
      return userService.createOrUpdate(userToBeUpdate, roles);
    }
    return userService.createOrUpdate(userToBeUpdate);
  }

  private Set<Role> getRoles(@RequestBody UserDto userDto) {
    List<RoleDto> userRoles = userDto.getRoles();
    if (CollectionUtils.isEmpty(userRoles)) {
      return null;
    }
    return userRoles.stream().
            filter(roleDto -> roleDto != null && roleDto.getRoleId() != 0)
            .filter(roleDto -> roleService.isRoleExist(roleDto.getRoleId()))
            .map(roleDto -> roleService.getRoleById(roleDto.getRoleId()).get())
            .collect(Collectors.toSet());
  }


  private Resource<UserDto> mappedUser(User user) {
    Link selfLink = linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel();
    UserDto userDto = modelMapper.map(user, UserDto.class);
    Link allRoles = entityLinks.linkToCollectionResource(RoleDto.class).withRel("all-roles");
    Link allBankAccount = linkTo(methodOn(RecipientBankAcctController.class).getRecipientByUserID(user.getId(), new PageRequest(0, 20))).withRel("all-bank-account");
    Link roleLink = linkTo(methodOn(RoleController.class).getRolesByUserId(user.getId(), new PageRequest(0, 20))).withRel("role");
    Link allPukGroups = entityLinks.linkToCollectionResource(PukGroupDto.class).withRel("all-puk-group");
    Link pukGroupByUser = linkTo(methodOn(PukGroupController.class).getPukGroupByUserId(user.getId(), new PageRequest(0, 20))).withRel("puk-group");
    return new Resource<>(userDto, selfLink, roleLink, allRoles, pukGroupByUser, allPukGroups, allBankAccount);
  }


}
