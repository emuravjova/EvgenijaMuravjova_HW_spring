package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.web.dto.SaleInfo;
import org.hamcrest.core.IsNull;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;

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
        CarSaleDetails carToAdd = new CarSaleDetails(1L, car, 12000, "Den 0501234567");
        CarSaleDetails storedCar = carService.addCar(carToAdd);
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, hasItem(storedCar));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void shouldGetCarDetailsById() {
        Car car = new Car("BMW","2010");
        CarSaleDetails expectedCarDetails = carService.addCar(new CarSaleDetails(1L, car, 12000, "Den 0501234567"));
        assertThat(carService.getCarDetailsById(expectedCarDetails.getId()), equalTo(expectedCarDetails));
    }

    @Test
    public void shouldReturnNullWhenNoCarFound() {
        assertThat(carService.getCarDetailsById(100500), IsNull.nullValue());
    }

    @Test
    public void shouldDeleteCarById() {
        Car car1 = new Car("BMW","2010");
        Car car2 = new Car("Lexus","2018");
        CarSaleDetails carToDelete = carService.addCar(new CarSaleDetails(1L, car1, 12000, "Den 0501234567"));
        CarSaleDetails storedCar = carService.addCar(new CarSaleDetails(2L, car2, 25000, "Ron 0502345678"));
        carService.deleteCarById(carToDelete.getId());
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, not(hasItem(carToDelete)));
        assertThat(availableCars, hasItem(storedCar));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test (dependsOnMethods = {"carShouldBeStored"})
    public void shouldNotDeleteAnyCarIfNoSuchId() {
        Car car = new Car("BMW","2010");
        CarSaleDetails storedCar = carService.addCar(new CarSaleDetails(1L, car, 12000, "Den 0501234567"));
        carService.deleteCarById(100500);
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, hasItem(storedCar));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void carShouldBeStored() {
        Car car = new Car("BMW","2010");
        CarSaleDetails carToStore = carService.addCar(new CarSaleDetails(1L, car, 12000, "Den 0501234567"));
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(carToStore.getId(), greaterThan(0L));
        assertThat(availableCars, hasItem(carToStore));
        assertThat(availableCars, iterableWithSize(1));
    }
}