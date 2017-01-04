package com.skwarek.blog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Michal on 04.10.2016.
 */
@Controller
public class LoginController {

    private static final String VIEWS_LOGIN_FORM = "registration/login";

    @RequestMapping(value = "/accounts/login", method = RequestMethod.GET)
    public String initLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {

        if (error != null) {
            model.addAttribute("error", true);
        }

        return VIEWS_LOGIN_FORM;
    }
}

