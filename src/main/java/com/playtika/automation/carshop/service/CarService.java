package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;

import java.util.Collection;
import java.util.Map;

public interface CarService {
    Collection<CarSaleDetails> getAllCars();
    CarSaleDetails getCarDetailsById(long id);
    void deleteCarById(long id);
    CarSaleDetails addCar(CarSaleDetails carToAdd);
}
