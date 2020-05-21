package com.mms.img.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstRestController {

    @GetMapping("/")
    public String index(){
        return "<div style='text-align: center; font-size: 5rem'>Welcome to our AwESoMe Image Macro Generator™</div>";
    }
}
