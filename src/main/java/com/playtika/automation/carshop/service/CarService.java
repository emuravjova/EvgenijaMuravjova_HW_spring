package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface CarService {
    Collection<CarSaleDetails> getAllCars();
    Optional<CarSaleDetails> getCarDetailsById(long id);
    boolean deleteCarById(long id);
    long addCar(CarSaleDetails carToAdd);
}
