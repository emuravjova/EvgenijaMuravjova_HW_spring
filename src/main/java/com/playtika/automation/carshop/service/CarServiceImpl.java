package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.web.CarNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
    public Map<String, Object> getCarDetailsById(long id) {
        if (CARS.containsKey(id)) {
            Map <String,Object> response = new HashMap<>();
            response.put("price",CARS.get(id).getPrice());
            response.put("contacts",CARS.get(id).getContacts());
            log.info("Car selling details with id = {} has been successfully returned", id);
            return response;
        }
        else {
            log.warn("Car with id = {} not found, sale details cannot be returned", id);
            throw new CarNotFoundException("Unable to find car with id " + id);
        }
    }

    @Override
    public void deleteCarById(long id) {
        if (CARS.containsKey(id)){
            CARS.remove(id);
            log.info("Car with id = {} has been successfully removed", id);
        }
        else log.warn("Car with id = {} does not exist, cannot be removed");
    }

    @Override
    public CarSaleDetails addCar(Car car, int price, String contacts) {
        CarSaleDetails carSaleDetails = new CarSaleDetails(ID.getAndAdd(1),car,price,contacts);
        CARS.put(carSaleDetails.getId(), carSaleDetails);
        log.info("New car with id {} has been added for selling: {}", CARS.get(carSaleDetails.getId()).getId(), CARS.get(carSaleDetails.getId()));
        return carSaleDetails;
    }

}
