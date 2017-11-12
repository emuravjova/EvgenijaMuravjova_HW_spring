package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.web.CarNotFoundException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class CarServiceImplTest {

    private CarService carService;

    @BeforeMethod
    public void init() {
        carService = new CarServiceImpl();
    }

    @Test
    public void ifNoCarsPresentShouldReturnEmptyResult() {
        assertThat(carService.getAllCars(), is(empty()));
    }

    @Test
    public void shouldReturnAllAvailableCars(){
        Car car = new Car("BMW","2010");
        CarSaleDetails storedCar = carService.addCar(car, 15000, "Ron 0982345678");
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, hasItem(storedCar));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void shouldGetCarDetailsById() {
        Map<String,Object> expectedCarDetails = new HashMap<>();
        expectedCarDetails.put("price",12000d);
        expectedCarDetails.put("contacts","Den 0501234567");
        Car car = new Car("BMW","2010");
        CarSaleDetails expectedCar = carService.addCar(car, (Double) expectedCarDetails.get("price"), (String) expectedCarDetails.get("contacts"));
        assertThat(carService.getCarDetailsById(expectedCar.getId()), equalTo(expectedCarDetails));
    }

    @Test (expectedExceptions = CarNotFoundException.class)
    public void shouldThrowExceptionWhenNoCarFound() {
        carService.getCarDetailsById(100500);
    }

    @Test
    public void shouldDeleteCarById() {
        Car car1 = new Car("BMW","2010");
        Car car2 = new Car("Lexus","2018");
        CarSaleDetails carToDelete = carService.addCar(car1, 12000, "Den 0501234567");
        CarSaleDetails storedCar = carService.addCar(car2, 15000, "Ron 0982345678");
        carService.deleteCarById(carToDelete.getId());
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, not(hasItem(carToDelete)));
        assertThat(availableCars, hasItem(storedCar));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test (dependsOnMethods = {"carShouldBeStored"})
    public void shouldDeleteCarByIdEvenIfNoSuchId() {
        Car car1 = new Car("BMW","2010");
        Car car2 = new Car("Lexus","2018");
        CarSaleDetails carToDelete = carService.addCar(car1, 12000, "Den 0501234567");
        CarSaleDetails storedCar = carService.addCar(car2, 15000, "Ron 0982345678");
        carService.deleteCarById(carToDelete.getId());
        carService.deleteCarById(carToDelete.getId());
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, not(hasItem(carToDelete)));
        assertThat(availableCars, hasItem(storedCar));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void carShouldBeStored() {
        Car car = new Car("BMW","2010");
        CarSaleDetails carToStore = carService.addCar(car, 12000, "Den 0501234567");
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, hasItem(carToStore));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void idForAddedCarShouldBeGenerated() {
        Car car = new Car("BMW","2010");
        car.setModel("2010");
        car.setName("BMW");
        CarSaleDetails carToStore = carService.addCar(car, 12000, "Den 0501234567");
        long idOfStoredCar = carToStore.getId();
        assertThat(idOfStoredCar, greaterThan(0L));
    }

}