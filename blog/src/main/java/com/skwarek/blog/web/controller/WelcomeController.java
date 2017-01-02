package com.skwarek.blog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michal on 02/01/2017.
 */
@Controller
public class WelcomeController {

    private static final String VIEWS_INDEX = "index";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome(Model model) {
        model.addAttribute("welcomeText", "Hello world");
        return VIEWS_INDEX;
    }
}
