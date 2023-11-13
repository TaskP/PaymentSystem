package com.example.payment.app.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller.
 */
@Controller
public class HomeController {

    /**
     * BuildProperties.
     */
    @Autowired
    private BuildProperties buildProperties;

    @GetMapping(path = { "/", "/bs", "/index.html", "/indexbs.html" })
    public String bootStrap(@AuthenticationPrincipal final UserDetails userDetails, final Model model) {
        model.addAttribute("applicationVersion", buildProperties.getVersion());
        model.addAttribute("applicationBuild", buildProperties.getTime());
        if (userDetails != null) {
            model.addAttribute("Username", userDetails.getUsername());
            final Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            if (authorities != null && authorities.size() > 0) {
                model.addAttribute("Role", authorities.iterator().next().getAuthority());
            }
        }
        return "homebs";
    }

}
