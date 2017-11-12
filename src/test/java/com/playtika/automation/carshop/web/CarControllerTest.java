package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.service.CarServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CarControllerTest {

    @Mock
    private CarServiceImpl carService;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CarController(carService))
                .build();
    }

    private MockMvc mockMvc;

    @Test
    public void shouldReturn200withJsonOnGetAllCars() throws Exception {
        Car car = new Car("BMW","2010");
        Set<CarSaleDetails> availableCars = Collections.singleton (new CarSaleDetails(1L,car,12000L,"Ron 0982345678"));
        when(carService.getAllCars()).thenReturn(availableCars);
        mockMvc.perform(get("/cars").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].car.name").value("BMW"))
                .andExpect(jsonPath("$[0].car.model").value("2010"))
                .andExpect(jsonPath("$[0].price").value(12000L))
                .andExpect(jsonPath("$[0].contacts").value("Ron 0982345678"));

    }

    @Test
    public void shouldReturn200withEmptyJsonOnGetAllCarsWhenNoCars() throws Exception {
        when(carService.getAllCars()).thenReturn(emptySet());
        mockMvc.perform(get("/cars").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("[]"));
    }

    @Test
    public void shouldReturn200withJsonOnGetCarDetailsById() throws Exception {
        Map<String,Object> expectedCarDetails = new HashMap<>();
        expectedCarDetails.put("price",12000d);
        expectedCarDetails.put("contacts","Den 0501234567");
        when(carService.getCarDetailsById(1)).thenReturn(expectedCarDetails);
        mockMvc.perform(get("/cars/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.price").value(12000))
                .andExpect(jsonPath("$.contacts").value("Den 0501234567"));
    }

    @Test
    public void shouldReturn404withMessageOnGetCarWhenNoCarFound() throws Exception {
        long id = 100500;
        when(carService.getCarDetailsById(id)).thenThrow(new CarNotFoundException("Unable to find car with id " + id));
        mockMvc.perform(get("/cars/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200onCarRemoving() throws Exception {
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isOk());
    }
//
//    @Test
//    void addCar() {
//    }

}