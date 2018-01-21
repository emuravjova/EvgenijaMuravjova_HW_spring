package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.dao.entity.*;
import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.Customer;
import com.playtika.automation.carshop.domain.SaleInfo;
import com.playtika.automation.carshop.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

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


    @Test
    public void shouldReturn400whenCarIdPriceAreAbsentOnCreateDeal() throws Exception {
        postDeal(null,null,createCustomerInJson("Den","1111"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400whenPriceAreEmptyOnCreateDeal() throws Exception {
        postDeal("","",createCustomerInJson("Den","1111"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400whenCustomerIsEmptyOnCreateDeal() throws Exception {
        postDeal(String.valueOf(20000),String.valueOf(1L),createCustomerInJson("",""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400whenNoCustomerProvidedOnCreateDeal() throws Exception {
        mockMvc.perform(post("/deal")
                .param("price", String.valueOf(20000))
                .param("id", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn200whenDealWasCreated() throws Exception {
        DealEntity deal = createDeal();
        Customer customer = new Customer(deal.getCustomer().getName(),deal.getCustomer().getContacts());
        Long carId = deal.getOffer().getCar().getId();
        when(carService.createDeal(carId, deal.getPrice(), customer))
                .thenReturn(Optional.of(deal));
        postDeal(String.valueOf(deal.getPrice()), String.valueOf(carId),createCustomerInJson(customer.getName(),customer.getContacts()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.offerId").value(1L));
    }

    @Test
    public void shouldReturn404whenCarIsNotOnSale() throws Exception {
        Customer customer = new Customer("Den","1111");
        when(carService.createDeal(1L, 1000, customer))
                .thenReturn(Optional.empty());
        postDeal(String.valueOf(1000), String.valueOf(1L),createCustomerInJson(customer.getName(),customer.getContacts()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200whenBestDealIsFound() throws Exception {
        DealEntity deal = createDeal();
        when(carService.findTheBestDeal(deal.getOffer().getId()))
                .thenReturn(Optional.of(deal));
        mockMvc.perform(get("/offer/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.offerId").value(1L));
    }

    @Test
    public void shouldReturn404whenBestDealNotFound() throws Exception {
        when(carService.findTheBestDeal(1L))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/offer/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200whenDealIsAccepted() throws Exception {
        when(carService.acceptDeal(1L))
                .thenReturn(true);
        mockMvc.perform(put("/acceptDeal/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn409whenDealCouldNotBeAccepted() throws Exception {
        when(carService.acceptDeal(1L))
                .thenReturn(false);
        mockMvc.perform(put("/acceptDeal/1"))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturn200whenDealIsRejected() throws Exception {
        mockMvc.perform(put("/rejectDeal/1"))
                .andExpect(status().isOk());
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

    private static DealEntity createDeal(){
        CarEntity car = new CarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = new SellerEntity(1L,"John doe", "0501234567");
        OfferEntity offer = new OfferEntity(car, seller, 20000);
        offer.setId(1L);
        CustomerEntity customer = new CustomerEntity(1L,"Den","1111");
        DealEntity deal = new DealEntity(1L, customer, offer, 25000, DealEntity.State.ACTIVE);
        return deal;
    }

    private static String createCustomerInJson(String name, String contacts) {
        return String.format("{ \"name\": \"%s\", \"contacts\":\"%s\"}", name, contacts);

    }

    private ResultActions postDeal(String price, String id, String body) throws Exception {
        return mockMvc.perform(post("/deal")
                .param("price", price)
                .param("carId", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));
    }
}