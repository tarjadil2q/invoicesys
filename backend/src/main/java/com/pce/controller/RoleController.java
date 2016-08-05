package com.pce.controller;

import com.pce.domain.dto.RoleCreationForm;
import com.pce.service.RoleService;
import com.pce.validator.RoleCreateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Leonardo Tarjadi on 11/02/2016.
 */
@Controller
public class RoleController {

    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleCreateValidator roleCreateValidator;

    @InitBinder("roleCreateForm")
    public void initBinder(WebDataBinder binder){
        binder.addValidators(roleCreateValidator);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @RequestMapping(value = "/role/create", method = RequestMethod.GET)
    public ModelAndView getRoleCreatePage(){
        RoleCreationForm roleCreationForm = new RoleCreationForm();
        return new ModelAndView("roleCreate", "roleCreateForm", roleCreationForm);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @RequestMapping(value = "/role/create", method = RequestMethod.POST)
    public String handleRoleCreateForm(@Valid @ModelAttribute("roleCreateForm") RoleCreationForm roleCreationForm, BindingResult bindingResult,
                                       Model model){
        if (bindingResult.hasErrors()){
            return "roleCreate";
        }
        try{
            roleService.create(roleCreationForm);
        }catch(DataIntegrityViolationException e){
            LOG.warn("Integrity Exception happen when creating new user", e);
            bindingResult.reject("data.integration.exception", "Data integrity exception");
            return "roleCreate";
        }
        return "redirect:/roles";
    }

}
