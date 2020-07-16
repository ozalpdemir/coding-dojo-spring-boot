package com.assignment.spring.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherErrorController implements ErrorController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String PATH = "/error";

    @GetMapping(PATH)
    public String error(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        LOGGER.info("Weather controller responded with an error: {}", status);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "500";
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}