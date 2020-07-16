package com.assignment.spring.advice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.assignment.spring.openweather.IncorrectResponseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WeatherControllerExceptionAdvice extends ResponseEntityExceptionHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(IncorrectResponseException.class)
  @ResponseBody
  ResponseEntity<Object> handleIncorrectWeatherResponseException(final IncorrectResponseException e,
      final WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", "Incorrect response from weather api");

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(HttpClientErrorException.NotFound.class)
  @ResponseBody
  ResponseEntity<Object> handleCityNotFoundException(final HttpClientErrorException.NotFound e,
      final WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", "Invalid city name");

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(HttpClientErrorException.class)
  @ResponseBody
  ResponseEntity<Object> handleIncorrectWeatherResponseException(final HttpClientErrorException e,
      final WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", "Internal error while fetching weather information");

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}