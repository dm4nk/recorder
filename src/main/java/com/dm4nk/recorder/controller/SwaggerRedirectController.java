package com.dm4nk.recorder.controller;

import org.springframework.web.bind.annotation.GetMapping;

public interface SwaggerRedirectController {
    @GetMapping("/")
    String redirectToSwagger();
}
