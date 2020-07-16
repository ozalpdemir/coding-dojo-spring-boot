package com.assignment.spring.service;

import com.assignment.spring.WeatherEntity;
import com.assignment.spring.openweather.IncorrectResponseException;
import com.assignment.spring.openweather.model.WeatherResponse;
import com.assignment.spring.repository.WeatherRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherRepository weatherRepository;

    /**
     * The url for the weather api
     */
    @Value("${weather.api.url}")
    private String weatherApiUrl;

    /**
     * The api token for the weather api
     */
    @Value("${weather.api.token}")
    private String weatherApiToken;

    public ResponseEntity<WeatherResponse> fetchCityWeather(final String city) {
        LOGGER.info("Fetching weather data for city {}", city);

        String url = weatherApiUrl.replace("{city}", city).replace("{appid}", weatherApiToken);
        return restTemplate.getForEntity(url, WeatherResponse.class);
    }

    public WeatherEntity saveCityWeather(final ResponseEntity<WeatherResponse> weatherResponse) {

        return convertAndSaveResponse(weatherResponse.getBody());
    }

    /**
     * Maps the weatherResponse content to a Weather Entity
     * 
     * @param response The response from the weather api
     * @return the weather entity that is persisted in the database
     */
    private WeatherEntity convertAndSaveResponse(WeatherResponse response) {

        if (response.getSys() == null || response.getMain() == null) {
            throw new IncorrectResponseException("The response from the openweather api is invalid");
        }

        WeatherEntity entity = new WeatherEntity();
        entity.setCity(response.getName());
        entity.setCountry(response.getSys().getCountry());
        entity.setTemperature(response.getMain().getTemp());

        return weatherRepository.save(entity);
    }

}