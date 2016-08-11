package com.pce.controller;

import com.google.common.base.Preconditions;
import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.domain.dto.*;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
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

  @Autowired
  public UserController(UserCreateValidator userCreateValidator, UserService userService,
                        RoleService roleService,
                        UserMapper userMapper,
                        EntityLinks entityLinks) {
    this.userCreateValidator = userCreateValidator;
    this.userService = userService;
    this.roleService = roleService;
    this.userMapper = userMapper;
    this.entityLinks = entityLinks;
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
  public HttpEntity<Resource<DomainObjectDTO>> createUser(@RequestBody UserDto userDto) {
    Preconditions.checkArgument(userDto != null, "User cannot be null");

    User user = userMapper.mapDtoIntoEntity(userDto);
    if (userService.isUserExists(userDto.getEmail())) {
      return new ResponseEntity(new Resource<>(new ErrorDto("User already exists")), HttpStatus.CONFLICT);
    }
    List<RoleDto> userRoles = userDto.getRoles();

    Set<Role> roles = userRoles.stream().distinct().
            filter(roleDto -> roleDto != null && roleDto.getRoleId() != 0)
            .filter(roleDto -> roleService.isRoleExist(roleDto.getRoleId()))
            .map(roleDto -> roleService.getRoleById(roleDto.getRoleId()).get())
            .collect(Collectors.toSet());
    try {
      user = userService.create(user, roles);
      userDto.setLink(user.getId(), USER);
      Link resourceLink = userDto.getLink("self");
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setLocation(URI.create(resourceLink.getHref()));
      return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    } catch (DataIntegrityViolationException e) {
      return new ResponseEntity(new Resource<>(new ErrorDto("Data integrity exception " + e.getMessage())), HttpStatus.NOT_FOUND);
    }


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
      userService.create(form);
    } catch (DataIntegrityViolationException e) {
      LOG.warn("Integrity Exception happen when creating new user", e);
      bindingResult.reject("data.integration.exception", "Data integrity exception");
      model.addAttribute("userCreateForm", userCreateForm);
      return "userCreate";
    }
    return "redirect:/users";
  }


}
