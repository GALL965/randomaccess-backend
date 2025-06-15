package com.gael.newbackend.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String test() {
        return "OK TEST CORS";
    }
}
