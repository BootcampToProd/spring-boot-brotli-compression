package com.bootcamptoprod.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrotliController {

    @GetMapping("/large-response")
    public String getLargeResponse() {
        return "Hello, World! ".repeat(1000000);
    }
}
