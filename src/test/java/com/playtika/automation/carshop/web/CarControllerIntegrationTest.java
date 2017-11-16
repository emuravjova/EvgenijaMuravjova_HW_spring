package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.service.CarService;
import com.playtika.automation.carshop.web.dto.SaleInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
public class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;


    @Test
    public void shouldReturn200withJsonOnGetAllCars() throws Exception {
        Car car = new Car("BMW","2010");
        Set<CarSaleDetails> availableCars = Collections.singleton (new CarSaleDetails(1L, car, 12000, "Ron 0982345678"));
        when(carService.getAllCars()).thenReturn(availableCars);
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].car.name").value("BMW"))
                .andExpect(jsonPath("$[0].car.model").value("2010"))
                .andExpect(jsonPath("$[0].price").value(12000))
                .andExpect(jsonPath("$[0].contacts").value("Ron 0982345678"));

    }

    @Test
    public void shouldReturn200withEmptyJsonOnGetAllCarsWhenNoCars() throws Exception {
        when(carService.getAllCars()).thenReturn(emptySet());
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("[]"));
    }

    @Test
    public void shouldReturn200withJsonOnGetCarDetailsById() throws Exception {
        Car car = new Car("BMW","2010");
        CarSaleDetails expectedCarDetails = new CarSaleDetails(1L, car, 12000, "Den 0501234567");
        when(carService.getCarDetailsById(1)).thenReturn(expectedCarDetails);
        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.price").value(12000))
                .andExpect(jsonPath("$.contacts").value("Den 0501234567"));
    }

    @Test
    public void shouldReturn404onGetCarWhenNoCarFound() throws Exception {
        long id = 100500;
        when(carService.getCarDetailsById(id)).thenThrow(new CarNotFoundException("Unable to find car with id " + id));
        mockMvc.perform(get("/cars/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200onCarRemoving() throws Exception {
        doNothing().when(carService).deleteCarById(1L);
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn200withJsonOnAddCar() throws Exception {
        Car car = new Car("BMW", "2010");
        CarSaleDetails carSaleDetails = new CarSaleDetails(1L, car, 25000, "Bob 0969876543");
        when(carService.addCar(carSaleDetails)).thenReturn(carSaleDetails);
        mockMvc.perform(post("/cars?price=25000&contacts=Bob 0969876543")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCarInJson(car.getName(),car.getModel())))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.car.name").value("BMW"))
                .andExpect(jsonPath("$.car.model").value("2010"))
                .andExpect(jsonPath("$.price").value(25000))
                .andExpect(jsonPath("$.contacts").value("Bob 0969876543"));
    }

    @Test
    public void shouldReturn400whenNoCarProvidedOnAddCar() throws Exception {
        Car car = new Car("BMW", "2010");
        CarSaleDetails carSaleDetails = new CarSaleDetails(1L, car, 25000, "Bob 0969876543");
        when(carService.addCar(carSaleDetails)).thenReturn(carSaleDetails);
        mockMvc.perform(post("/cars?price=25000&contacts=Bob 0969876543")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        verify(carService, never()).addCar(carSaleDetails);
    }

    @Test
    public void shouldReturn400whenEmptyCarProvidedOnAddCar() throws Exception {
        Car car = new Car("BMW", "2010");
        CarSaleDetails carSaleDetails = new CarSaleDetails(1L, car, 25000, "Bob 0969876543");
        when(carService.addCar(carSaleDetails)).thenReturn(carSaleDetails);
        mockMvc.perform(post("/cars?price=25000&contacts=Bob 0969876543")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCarInJson("","")))
                .andExpect(status().isBadRequest());
        verify(carService, never()).addCar(carSaleDetails);
    }

    @Test
    public void shouldReturn400whenNoCarDetailsOnAddCar() throws Exception {
        Car car = new Car("BMW", "2010");
        CarSaleDetails carSaleDetails = new CarSaleDetails(1L, car, 25000, "Bob 0969876543");
        when(carService.addCar(carSaleDetails)).thenReturn(carSaleDetails);
        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCarInJson(car.getName(),car.getModel())))
                .andExpect(status().isBadRequest());
        verify(carService, never()).addCar(carSaleDetails);
    }

    @Test
    public void shouldReturn400whenCarDetailsAreEmptyOnAddCar() throws Exception {
        Car car = new Car("BMW", "2010");
        CarSaleDetails carSaleDetails = new CarSaleDetails(1L, car, 25000, "Bob 0969876543");
        when(carService.addCar(carSaleDetails)).thenReturn(carSaleDetails);
        mockMvc.perform(post("/cars?price=&contacts=")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCarInJson(car.getName(),car.getModel())))
                .andExpect(status().isBadRequest());
        verify(carService, never()).addCar(carSaleDetails);
    }

    private static String createCarInJson (String name, String model) {
        return "{ \"name\": \"" + name + "\", " + "\"model\":\"" + model + "\"}";

    }

    }