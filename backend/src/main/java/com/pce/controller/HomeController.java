package com.pce.controller;

import freemarker.ext.dom.NodeModel;
import freemarker.template.TemplateNodeModel;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

/**
 * Created by Leonardo Tarjadi on 8/02/2016.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String getHomePage(Model model) {
        try{
            model.addAttribute("result", NodeModel.parse(new File("data.xml"))) ;
        }catch(Exception e){

        }
        return "home";
    }
}
