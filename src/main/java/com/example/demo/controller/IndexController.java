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

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/", "/index"})
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();

        if (authService.hasRole(AuthService.ROLE_USER)) {
            mv.setViewName("user/home");
        } else {
            mv.setViewName("index");
        }
        return mv;
    }

}