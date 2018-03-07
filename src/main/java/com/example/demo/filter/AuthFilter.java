package com.example.demo.filter;

import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AuthFilter {

    @Autowired
    AuthService authService;

    @ModelAttribute("__username")
    public String auth(Model model) {
        return authService.getUsername();
    }

}
