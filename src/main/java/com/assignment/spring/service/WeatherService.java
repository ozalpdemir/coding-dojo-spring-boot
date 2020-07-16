package com.assignment.spring.service;

import com.assignment.spring.WeatherEntity;
import com.assignment.spring.openweather.model.WeatherResponse;

import org.springframework.http.ResponseEntity;

public interface WeatherService {

    /**
     * Fetches the weather data for a given city
     * 
     * @param city The city to get the weather data for
     * @return a weather response wrapped as a resposne entity
     */
    ResponseEntity<WeatherResponse> fetchCityWeather(final String city);

    /**
     * Stores the weather data for a given city and returns it.
     * 
     * @param weatherResponse The weather reponse from the weather api
     * @return The stored weather entity
     */
    WeatherEntity saveCityWeather(final ResponseEntity<WeatherResponse> weatherResponse);

}