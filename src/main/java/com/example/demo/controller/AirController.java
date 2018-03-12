package com.example.demo.controller;

import com.example.demo.data.PassengerSeat;
import com.example.demo.service.PassengerSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AirController {

    @Autowired
    PassengerSeatService passengerSeatService;

    @GetMapping(value = {"/air"})
    public String air(ModelMap model) {
        PassengerSeat cmd = new PassengerSeat();
        passengerSeatService.fill(cmd);
        model.addAttribute("cmd", cmd);
        return "user/air";
    }

    @PostMapping(value = {"/air/next"})
    public String submitAir(@ModelAttribute PassengerSeat cmd, ModelMap model) {
        passengerSeatService.fill(cmd);
        model.addAttribute("cmd", cmd);
        return "user/air";
    }

}
