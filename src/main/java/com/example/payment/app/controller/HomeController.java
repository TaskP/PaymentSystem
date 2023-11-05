package com.example.payment.app.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index(final Principal principal) {
        // return principal != null ? "home" : "login";
        return "home";
    }
}
