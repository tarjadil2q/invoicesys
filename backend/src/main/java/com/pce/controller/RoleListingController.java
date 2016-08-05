package com.pce.controller;

import com.pce.domain.Role;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.exception.InvalidResourceException;
import com.pce.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 1/03/2016.
 */
@RestController
public class RoleListingController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/roles", produces = "application/json")
    public Page<DomainObjectDTO> getAllRoles(@RequestParam("page") int page){
        Pageable pageRequest = new PageRequest(page - 1, 10);
        Page<DomainObjectDTO> allAvailableRoles = roleService.getAllAvailableRoles(pageRequest);
        if (allAvailableRoles.getTotalElements() == 0){
            return null;
        }
        if (page > allAvailableRoles.getTotalPages()) {
            throw new InvalidResourceException("Requested page " + page + " is greater than total available page");
        }

        return allAvailableRoles;
    }
}
