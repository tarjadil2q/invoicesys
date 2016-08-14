package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.CurrentUser;
import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.domain.dto.*;
import com.pce.service.CurrentUserService;
import com.pce.service.RoleService;
import com.pce.service.UserService;
import com.pce.service.mapper.UserMapper;
import com.pce.validator.UserCreateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Leonardo Tarjadi on 7/02/2016.
 */
@RestController
@RequestMapping("/api/pce/user")
@ExposesResourceFor(UserDto.class)

public class UserController {

  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  public static final String USER = "/user";

  private UserService userService;
  private RoleService roleService;
  private UserMapper userMapper;
  private UserCreateValidator userCreateValidator;
  private EntityLinks entityLinks;
  private CurrentUserService currentUserService;

  @Autowired
  public UserController(UserCreateValidator userCreateValidator, UserService userService,
                        RoleService roleService,
                        UserMapper userMapper,
                        EntityLinks entityLinks,
                        CurrentUserService currentUserService) {
    this.userCreateValidator = userCreateValidator;
    this.userService = userService;
    this.roleService = roleService;
    this.userMapper = userMapper;
    this.entityLinks = entityLinks;
    this.currentUserService = currentUserService;
  }

  @InitBinder("userCreateForm")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(userCreateValidator);
  }


  @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id).orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", id)));
    UserDto userDto = (UserDto) userMapper.mapEntityIntoDTO(user);
    Link linkForUser = entityLinks.linkToSingleResource(UserDto.class, userDto.getUserId());

    Resource<DomainObjectDTO> userResource = new Resource<>(userDto, linkForUser);
    return new ResponseEntity<>(userResource, HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")

  public HttpEntity<PagedResources<DomainObjectDTO>> getUsers(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<User> allUsers = userService.getAllUsers(pageRequest);
    Page<DomainObjectDTO> userDtos = userMapper.mapEntityPageIntoDTOPage(pageRequest, allUsers);
    return new ResponseEntity<>(assembler.toResource(userDtos), HttpStatus.OK);
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
    userDto.setLink(user.getId(), USER);
    Link resourceLink = userDto.getLink("self");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(URI.create(resourceLink.getHref()));
    return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updateUserAndRole(@PathVariable("id") long id,
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

    userDto.setLink(updatedUser.getId(), USER);
    Link resourceLink = userDto.getLink("self");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(URI.create(resourceLink.getHref()));
    return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);

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

    /*@PreAuthorize("hasAuthority('Admin')")
    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage() {
        UserCreationForm userCreationForm = new UserCreationForm();
        userCreationForm.setRoles(roleService.getAllAvailableRoles());
        return new ModelAndView("userCreate", "userCreateForm", userCreationForm);
    }*/

  @PreAuthorize("hasAuthority('Admin')")
  @RequestMapping(value = "/user/create", method = RequestMethod.POST)
  public String handleUserCreateForm(@Valid @ModelAttribute("userCreateForm") UserCreationForm form, BindingResult bindingResult,
                                     Model model) {
    List<Role> allAvailableRoles = roleService.getAllAvailableRoles();
    UserCreationForm userCreateForm = (UserCreationForm) model.asMap().get("userCreateForm");
    userCreateForm.setRoles(allAvailableRoles);
    LOG.debug("Creating new user with []", userCreateForm);
    if (bindingResult.hasErrors()) {
      model.addAttribute("userCreateForm", userCreateForm);
      return "userCreate";
    }
    try {
      userService.createOrUpdate(form);
    } catch (DataIntegrityViolationException e) {
      LOG.warn("Integrity Exception happen when creating new user", e);
      bindingResult.reject("data.integration.exception", "Data integrity exception");
      model.addAttribute("userCreateForm", userCreateForm);
      return "userCreate";
    }
    return "redirect:/users";
  }


}
