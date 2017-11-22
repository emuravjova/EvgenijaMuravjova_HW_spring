package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.domain.CarSaleDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class CarServiceImpl implements CarService {

    private final Map<Long,CarSaleDetails> CARS = new ConcurrentHashMap<>();

    private final AtomicLong ID = new AtomicLong(1);

    @Override
    public Collection<CarSaleDetails> getAllCars() {
        log.info("All available cars with selling details has been returned");
        return CARS.values();
    }

    @Override
    public Optional<CarSaleDetails> getCarDetailsById(long id) {
        Optional<CarSaleDetails> carDetails = Optional.ofNullable(CARS.get(id));
        log.info("Following car details has been returned by id {}: {}", id, carDetails);
        return carDetails;
    }


    @Override
    public boolean deleteCarById(long id) {
        if (CARS.remove(id) == null) {
            log.warn("Car with id = {} does not exist, cannot be removed", id);
            return false;
        }
        log.info("Car with id = {} has been successfully removed", id);
        return true;
    }

    @Override
    public long addCar(CarSaleDetails carToAdd) {
        long id = ID.getAndAdd(1);
        carToAdd.setId(id);
        CARS.put(carToAdd.getId(), carToAdd);
        log.info("New car with id {} has been added for selling: {}", id, carToAdd);
        return id;
    }

}
