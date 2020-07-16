package com.assignment.spring.service;

import static org.junit.Assert.assertEquals;

import com.assignment.spring.Application;
import com.assignment.spring.WeatherEntity;
import com.assignment.spring.openweather.IncorrectResponseException;
import com.assignment.spring.openweather.model.Main;
import com.assignment.spring.openweather.model.Sys;
import com.assignment.spring.openweather.model.WeatherResponse;
import com.assignment.spring.repository.WeatherRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private WeatherRepository weatherRepository;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.token}")
    private String weatherApiToken;

    @Test
    public void shouldFetchWeatherForAGivenCity() throws Exception {
        final WeatherResponse expectedWeatherResponse = new WeatherResponse();
        final ResponseEntity<WeatherResponse> expectedResponseEntity = ResponseEntity.ok(expectedWeatherResponse);

        final String expectedUrl = weatherApiUrl.replace("{city}", "Amsterdam").replace("{appid}", weatherApiToken);

        // Return the prepared response when the mocked restTemplate is called
        Mockito.when(restTemplate.getForEntity(Mockito.eq(expectedUrl), Mockito.eq(WeatherResponse.class)))
                .thenReturn(expectedResponseEntity);

        // Do the actual call to the service method
        weatherService.fetchCityWeather("Amsterdam");

        Mockito.verify(restTemplate, Mockito.times(1)).getForEntity(Mockito.anyString(),
                Mockito.eq(WeatherResponse.class));

    }

    @Test
    public void shouldSaveTheFetchedWeatherResponse() throws Exception {
        final String expectedName = "Name of the city";
        final String expectedCountry = "Name of the country";
        final Double expectedTemperature = 32d;

        final WeatherResponse weatherResponse = new WeatherResponse();
        final Main main = new Main();
        main.setTemp(expectedTemperature);
        final Sys sys = new Sys();
        sys.setCountry(expectedCountry);

        weatherResponse.setMain(main);
        weatherResponse.setSys(sys);
        weatherResponse.setName(expectedName);
        final ResponseEntity<WeatherResponse> weatherResponseEntity = ResponseEntity.ok(weatherResponse);

        // Return the prepared response when the mocked repository is called
        Mockito.when(weatherRepository.save(Mockito.any(WeatherEntity.class)))
                .thenReturn(Mockito.any(WeatherEntity.class));

        // Do the actual call to the service method
        weatherService.saveCityWeather(weatherResponseEntity);

        // As we are only testing the service, there is no need to test the response of
        // the save call.
        // It is more important to test the input of the repository save
        ArgumentCaptor<WeatherEntity> argumentCaptor = ArgumentCaptor.forClass(WeatherEntity.class);
        Mockito.verify(weatherRepository).save(argumentCaptor.capture());
        WeatherEntity capturedArgument = argumentCaptor.getValue();
        assertEquals("City should be the same", expectedName, capturedArgument.getCity());
        assertEquals("Country should be the same", expectedCountry, capturedArgument.getCountry());
        assertEquals("Temperature should be the same", expectedTemperature, capturedArgument.getTemperature());

        Mockito.verify(weatherRepository, Mockito.times(1)).save(Mockito.any(WeatherEntity.class));

    }

    @Test(expected = IncorrectResponseException.class)
    public void saveShouldThowExceptionOnEmptyResponse() throws Exception {
        final WeatherResponse weatherResponse = new WeatherResponse();
        final ResponseEntity<WeatherResponse> weatherResponseEntity = ResponseEntity.ok(weatherResponse);

        // Do the actual call to the service method
        weatherService.saveCityWeather(weatherResponseEntity);

        Mockito.verifyZeroInteractions(weatherRepository);

    }

    @Test(expected = IncorrectResponseException.class)
    public void saveShouldThowExceptionOnEmptySys() throws Exception {
        final WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setMain(new Main());
        final ResponseEntity<WeatherResponse> weatherResponseEntity = ResponseEntity.ok(weatherResponse);

        // Do the actual call to the service method
        weatherService.saveCityWeather(weatherResponseEntity);

        Mockito.verifyZeroInteractions(weatherRepository);

    }

    @Test(expected = IncorrectResponseException.class)
    public void saveShouldThowExceptionOnEmptyMain() throws Exception {
        final WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setSys(new Sys());
        final ResponseEntity<WeatherResponse> weatherResponseEntity = ResponseEntity.ok(weatherResponse);

        // Do the actual call to the service method
        weatherService.saveCityWeather(weatherResponseEntity);

        Mockito.verifyZeroInteractions(weatherRepository);

    }
}