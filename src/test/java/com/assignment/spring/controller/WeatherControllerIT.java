package com.assignment.spring.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import com.assignment.spring.Application;
import com.assignment.spring.WeatherEntity;
import com.assignment.spring.openweather.model.WeatherResponse;
import com.assignment.spring.repository.WeatherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerIT {

    private static final String WEATHER_URL = "http://localhost:{p}/weather?city={q}";

    @MockBean
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("classpath:openweather/responses/weather_for_london.json")
    private Resource londonWeatherResponse;

    @Before
    public void setUp() {
        weatherRepository.deleteAll();
    }

    @Test
    public void shouldReturnWeatherForCity() throws Exception {
        // Prepare response object for the mocked restTemplate
        final WeatherResponse weatherResponse = objectMapper.readValue(londonWeatherResponse.getFile(),
                WeatherResponse.class);

        final ResponseEntity<WeatherResponse> responseEntityForMock = ResponseEntity.ok(weatherResponse);

        // Return the prepared response when the mocked restTemplate is called
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(WeatherResponse.class)))
                .thenReturn(responseEntityForMock);

        // Do the actual call to the controller
        ResponseEntity<WeatherEntity> responseEntity = this.testRestTemplate.getForEntity(WEATHER_URL,
                WeatherEntity.class, port, "London");

        // Our expectations
        final String expectedCity = "London";
        final String expectedCountry = "GB";
        final Double expectedTemperature = Double.valueOf(292.17);

        // Check the response on valid data
        assertEquals("The response should be OK", HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The city in the response is not correct", expectedCity, responseEntity.getBody().getCity());
        assertEquals("The country in the response is not correct", expectedCountry,
                responseEntity.getBody().getCountry());
        assertTrue("The temperature in the response is not correct",
                expectedTemperature.equals(responseEntity.getBody().getTemperature()));

        // Verify that the response is also saved on database
        final Optional<WeatherEntity> savedWeatherEntity = weatherRepository.findById(responseEntity.getBody().getId());
        assertTrue("The entity should be correctly saved", savedWeatherEntity.isPresent());
        assertEquals("The id's should be the same", responseEntity.getBody().getId(), savedWeatherEntity.get().getId());
        assertEquals("The city names should be the same", expectedCity, savedWeatherEntity.get().getCity());
        assertEquals("The country names should be the same", expectedCountry, savedWeatherEntity.get().getCountry());
        assertEquals("The temperatures should be the same", expectedTemperature,
                savedWeatherEntity.get().getTemperature());

    }

}