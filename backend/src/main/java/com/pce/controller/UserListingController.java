package com.pce.controller;

import com.pce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Leonardo Tarjadi on 8/02/2016.
 */
@Controller
public class UserListingController {

    private UserService userService;

    @Autowired
    public UserListingController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users")
    public ModelAndView getUsersPage() {
        return new ModelAndView("users", "users", userService.getAllUsers());
    }
}
