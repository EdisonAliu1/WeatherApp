package com.project.weatherapp.controller;

import com.project.weatherapp.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherController {
    @Value("${api.key}")
    private String apiKey;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam("city") String city, Model model) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
            WeatherResponse weatherResponse = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && weatherResponse != null) {
                model.addAttribute("city", weatherResponse.getName());
                model.addAttribute("country", weatherResponse.getSys().getCountry());
                model.addAttribute("weatherDescription", weatherResponse.getWeather().get(0).getDescription());
                model.addAttribute("temperature", weatherResponse.getMain().getTemp());
                model.addAttribute("humidity", weatherResponse.getMain().getHumidity());
                model.addAttribute("windSpeed", weatherResponse.getWind().getSpeed());
            } else {
                model.addAttribute("error", "Could not retrieve weather data. Please check the city name.");
            }
        } catch (RestClientException e) {
            model.addAttribute("error", "An error occurred while fetching the weather data. Please try again.");
        }

        return "index";
    }
}
