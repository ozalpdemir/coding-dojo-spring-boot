package com.assignment.spring.controller;

import javax.validation.constraints.NotBlank;

import com.assignment.spring.WeatherEntity;
import com.assignment.spring.service.WeatherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public WeatherEntity weather(final @RequestParam(name = "city") @NotBlank String city) {
        LOGGER.info("Getting weather data for {}", city);

        var weatherResponse = weatherService.fetchCityWeather(city);
        LOGGER.debug("The weather service fetched data {}", weatherResponse);
        return weatherService.saveCityWeather(weatherResponse);

    }
}
