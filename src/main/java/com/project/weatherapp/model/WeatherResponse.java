package com.project.weatherapp.model;



import lombok.Data;

import java.util.List;


@Data
public class WeatherResponse {
    private String name;
    private SystemName sys;
    private List<Weather> weather;
    private Main main;
    private Wind wind;

}


