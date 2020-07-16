package com.assignment.spring.repository;

import com.assignment.spring.WeatherEntity;

import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {
}
