package com.example.demo.controller;

import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    AuthService authService;

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return authService.isAuthorised() ? "user/home" : "index";
    }

}
