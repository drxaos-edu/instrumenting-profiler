package com.example.demo.controller;

import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    AuthService authService;

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return authService.isAuthorised() ? "user/home" : "index";
    }

}