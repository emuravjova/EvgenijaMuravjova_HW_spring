package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.SaleInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CarServiceImplTest {

    private CarService carService;

    @Autowired
    private EntityManager em;

    @Before
    public void init() {
        carService = new CarServiceImpl(em);
    }

    @Test
    public void ifNoCarsPresentShouldReturnEmptyResult() {
        assertThat(carService.getAllCars(), is(empty()));
    }

    @Test
    public void shouldReturnAllAvailableCars(){
        Car car = new Car("AS123","BMW", 2007,"blue");
        CarSaleDetails carToAdd = new CarSaleDetails(1L, car, new SaleInfo(12000, "Den 0501234567"));
        carService.addCar(carToAdd);
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, hasItem(carToAdd));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void shouldGetCarDetailsById() {
        Car car = new Car("AS123","BMW", 2007,"blue");
        CarSaleDetails carToAdd = new CarSaleDetails(1L, car, new SaleInfo(12000, "Den 0501234567"));
        long id = carService.addCar(carToAdd);
        assertThat(carService.getCarDetailsById(id), equalTo(java.util.Optional.of(carToAdd)));
    }

    @Test
    public void shouldReturnEmptyOptionalWhenNoCarFound() {
        assertTrue(!carService.getCarDetailsById(100500).isPresent());
    }

    @Test
    public void shouldDeleteCarById() {
        Car car1 = new Car("AS123","BMW", 2007,"blue");
        CarSaleDetails carToAdd = new CarSaleDetails(1L, car1, new SaleInfo(12000, "Den 0501234567"));
        Car car2 = new Car("AS124","Lexus", 2007,"green");
        CarSaleDetails carToDelete = new CarSaleDetails(2L, car2, new SaleInfo(25000, "Ron 0502345678"));
        carService.addCar(carToAdd);
        carService.addCar(carToDelete);
        carService.deleteCarById(carToDelete.getId());
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, not(hasItem(carToDelete)));
        assertThat(availableCars, hasItem(carToAdd));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void shouldNotDeleteAnyCarIfNoSuchId() {
        Car car = new Car("AS123","BMW", 2007,"blue");
        CarSaleDetails storedCar = new CarSaleDetails(1L, car, new SaleInfo(12000, "Den 0501234567"));
        carService.addCar(storedCar);
        carService.deleteCarById(100500);
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(availableCars, hasItem(storedCar));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void carShouldBeStored() {
        Car car = new Car("AS123","BMW", 2007,"blue");
        CarSaleDetails storedCar = new CarSaleDetails(0L, car, new SaleInfo(12000, "Den 0501234567"));
        long id = carService.addCar(storedCar);
        Collection<CarSaleDetails> availableCars = carService.getAllCars();
        assertThat(id, greaterThan(0L));
        assertThat(availableCars, hasItem(storedCar));
        assertThat(availableCars, iterableWithSize(1));
    }
}