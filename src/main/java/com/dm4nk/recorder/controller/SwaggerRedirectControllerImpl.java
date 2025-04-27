package com.dm4nk.recorder.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class SwaggerRedirectControllerImpl implements SwaggerRedirectController {

    @Value("${app.swagger-redirect-url}")
    private String redirectUrl;

    @Override
    public String redirectToSwagger() {
        return redirectUrl;
    }
}
