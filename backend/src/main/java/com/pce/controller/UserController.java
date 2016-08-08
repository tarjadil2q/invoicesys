package com.pce.controller;

import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.UserCreationForm;
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
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Leonardo Tarjadi on 7/02/2016.
 */
@RestController
@RequestMapping("/api/pce")
public class UserController {

  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  private UserService userService;
  private RoleService roleService;
  private UserMapper userMapper;
  private UserCreateValidator userCreateValidator;

  @Autowired
  public UserController(UserCreateValidator userCreateValidator, UserService userService,
                        RoleService roleService,
                        UserMapper userMapper) {
    this.userCreateValidator = userCreateValidator;
    this.userService = userService;
    this.roleService = roleService;
    this.userMapper = userMapper;
  }

  @InitBinder("userCreateForm")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(userCreateValidator);
  }


  @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
  @RequestMapping("/user/{id}")
  public HttpEntity<DomainObjectDTO> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id).orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", id)));
    DomainObjectDTO userDto = userMapper.mapEntityIntoDTO(user);
    return new ResponseEntity<>(userDto, HttpStatus.FOUND);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")

  public HttpEntity<PagedResources<DomainObjectDTO>> getUsers(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<User> allUsers = userService.getAllUsers(pageRequest);
    Page<DomainObjectDTO> userDtos = userMapper.mapEntityPageIntoDTOPage(pageRequest, allUsers);
    return new ResponseEntity<>(assembler.toResource(userDtos), HttpStatus.FOUND);
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
