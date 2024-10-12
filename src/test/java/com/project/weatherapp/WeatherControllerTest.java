package com.project.weatherapp;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.project.weatherapp.controller.WeatherController;
import com.project.weatherapp.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeather_Success() throws Exception {
        // Set up your mock behavior
        String city = "London";
        WeatherResponse mockResponse = new WeatherResponse();
        // Populate mockResponse with the expected data

        when(restTemplate.getForEntity(anyString(), eq(WeatherResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Perform the request and verify the response
        mockMvc.perform(get("/weather").param("city", city))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("city"))
                .andExpect(view().name("index")); // Change as necessary
    }

    @Test
    public void testGetWeather_NotFound() throws Exception {
        String city = "InvalidCity";

        when(restTemplate.getForEntity(anyString(), eq(WeatherResponse.class)))
                .thenThrow(new RestClientException("Not Found"));

        mockMvc.perform(get("/weather").param("city", city))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("index")); // Change as necessary
    }
}
