package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.web.dto.SaleInfo;
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

//    @Override
//    public Map<String, Object> getCarDetailsById(long id) {
//        Map <String,Object> response = new HashMap<>();
//        if (CARS.containsKey(id)) {
//            response.put("price",CARS.get(id).getPrice());
//            response.put("contacts",CARS.get(id).getContacts());
//            log.info("Car selling details with id = {} has been successfully returned", id);
//            return response;
//        } else {
//            log.warn("Car with id = {} not found, sale details cannot be returned", id);
//            return response;
//        }
//    }

    @Override
    public CarSaleDetails getCarDetailsById(long id) {
        CarSaleDetails carDetails = CARS.get(id);
        if (carDetails != null) {
            log.info("Car selling details with id = {} has been successfully returned", id);
        }
        return carDetails;
    }


    @Override
    public void deleteCarById(long id) {
        if (CARS.remove(id) == null){
            log.warn("Car with id = {} does not exist, cannot be removed", id);
        } else {log.info("Car with id = {} has been successfully removed", id);}
    }

    @Override
    public CarSaleDetails addCar(CarSaleDetails carToAdd) {
        carToAdd.setId(ID.getAndAdd(1));
        CARS.put(carToAdd.getId(), carToAdd);
        log.info("New car with id {} has been added for selling: {}", CARS.get(carToAdd.getId()).getId(), CARS.get(carToAdd.getId()));
        return carToAdd;
    }

}
