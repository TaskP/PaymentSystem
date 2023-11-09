package com.example.payment.app.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller.
 */
@Controller
public class HomeController {

    @GetMapping(path = { "/", "/bs", "/index.html", "/indexbs.html" })
    public String bootStrap(final Principal principal) {
        return "homebs";
    }

    @GetMapping(path = { "/rjs", "/indexrjs.html" })
    public String reactJS(final Principal principal) {
        return "error";
    }

}
