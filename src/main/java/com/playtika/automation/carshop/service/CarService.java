package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.dao.entity.DealEntity;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.Customer;

import java.util.Collection;
import java.util.Optional;

public interface CarService {
    Collection<CarSaleDetails> getAllCars();

    Optional<CarSaleDetails> getCarDetailsById(long id);

    boolean deleteCarById(long id);

    long addCar(CarSaleDetails carToAdd);

    Optional<DealEntity> createDeal(Long id, int price, Customer customer);

    Optional<DealEntity> findTheBestDeal(Long id);

    boolean acceptDeal(long id);

    void rejectDeal(long id);
}
