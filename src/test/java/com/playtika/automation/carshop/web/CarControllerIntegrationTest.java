package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.SaleInfo;
import com.playtika.automation.carshop.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.Optional;
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

    private final static String NUMBER = "AS123";
    private final static String BRAND = "BMW";
    private final static Integer YEAR = 2007;
    private final static String COLOR = "blue";
    private final static Integer PRICE = 25000;
    private final static String CONTACTS = "Ron 0982345678";


    @Test
    public void shouldReturn200withJsonOnGetAllCars() throws Exception {
        Car car = new Car(NUMBER, BRAND, YEAR, COLOR);
        Set<CarSaleDetails> availableCars = Collections.singleton(new CarSaleDetails(1L, car, new SaleInfo(PRICE, CONTACTS)));
        when(carService.getAllCars()).thenReturn(availableCars);
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].carId").value(1L))
                .andExpect(jsonPath("$[0].car.number").value(NUMBER))
                .andExpect(jsonPath("$[0].car.brand").value(BRAND))
                .andExpect(jsonPath("$[0].car.year").value(YEAR))
                .andExpect(jsonPath("$[0].car.color").value(COLOR))
                .andExpect(jsonPath("$[0].saleInfo.price").value(PRICE))
                .andExpect(jsonPath("$[0].saleInfo.contacts").value(CONTACTS));

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
        Optional<CarSaleDetails> expectedCarDetails = Optional.of(createCarSaleDetails());
        when(carService.getCarDetailsById(1)).thenReturn(expectedCarDetails);
        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andExpect(jsonPath("$.contacts").value(CONTACTS));
    }

    @Test
    public void shouldReturn404onGetCarWhenNoCarFound() throws Exception {
        Optional<CarSaleDetails> expectedCarDetails = Optional.empty();
        when(carService.getCarDetailsById(100500)).thenReturn(expectedCarDetails);
        mockMvc.perform(get("/cars/100500"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200onCarRemoving() throws Exception {
        when(carService.deleteCarById(1L)).thenReturn(true);
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn204onCarRemoving() throws Exception {
        when(carService.deleteCarById(1L)).thenReturn(false);
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn200withJsonOnAddCar() throws Exception {
        CarSaleDetails carSaleDetails = createCarSaleDetails();
        when(carService.addCar(carSaleDetails)).thenReturn(1L);
        postCar(String.valueOf(PRICE), CONTACTS, createCarInJson(NUMBER, BRAND, YEAR, COLOR))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void shouldReturn400whenNoCarProvidedOnAddCar() throws Exception {
        CarSaleDetails carSaleDetails = createCarSaleDetails();
        when(carService.addCar(carSaleDetails)).thenReturn(1L);
        mockMvc.perform(post("/cars")
                .param("price", String.valueOf(carSaleDetails.getSaleInfo().getPrice()))
                .param("contacts", carSaleDetails.getSaleInfo().getContacts())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        verify(carService, never()).addCar(carSaleDetails);
    }

    @Test
    public void shouldReturn400whenEmptyCarProvidedOnAddCar() throws Exception {
        postCar(String.valueOf(PRICE), CONTACTS, createCarInJson("", "", null, ""))
                .andExpect(status().isBadRequest());
        verify(carService, never()).addCar(any());
    }

    @Test
    public void shouldReturn400whenNoCarDetailsOnAddCar() throws Exception {
        postCar(null, null, createCarInJson(NUMBER, BRAND, YEAR, COLOR))
                .andExpect(status().isBadRequest());
        verify(carService, never()).addCar(any());
    }

    @Test
    public void shouldReturn400whenCarDetailsAreEmptyOnAddCar() throws Exception {
        postCar("", "", createCarInJson(NUMBER, BRAND, YEAR, COLOR))
                .andExpect(status().isBadRequest());
        verify(carService, never()).addCar(any());
    }

    private static String createCarInJson(String number, String brand, Integer year, String color) {
        return String.format("{ \"number\": \"%s\", \"brand\":\"%s\", \"year\": %s, \"color\":\"%s\"}", number, brand, year, color);

    }

    private static CarSaleDetails createCarSaleDetails() {
        Car car = new Car(NUMBER, BRAND, YEAR, COLOR);
        CarSaleDetails createdCarDetails = new CarSaleDetails();
        createdCarDetails.setCar(car);
        createdCarDetails.setSaleInfo(new SaleInfo(PRICE, CONTACTS));
        return createdCarDetails;
    }

    private ResultActions postCar(String price, String contacts, String body) throws Exception {
        return mockMvc.perform(post("/cars")
                .param("price", price)
                .param("contacts", contacts)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));
    }
}