package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/cars")
public class CarController {

    private static final Map<Long,CarStorage> CARS = new HashMap<>();

    private final AtomicLong id = new AtomicLong(1);

    @GetMapping
    public Collection<CarStorage> getAllCars(){
        log.info("All available cars with selling details has been returned");
        return CARS.values();
    }

    @GetMapping(value = "/{id}")
    public Map<String,Object> getCarDetailsById(@PathVariable("id") long id){
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

    @DeleteMapping(value = "/{id}")
    public void deleteCarById(@PathVariable("id") long id){
        if (CARS.containsKey(id)){
        CARS.remove(id);
        log.info("Car with id = {} has been successfully removed", id);
        }
    }

    @PostMapping
    public CarStorage addCar(
            @RequestBody Car car,
            @RequestParam("price") double price,
            @RequestParam("contacts") String contacts)
    {
        CarStorage carStorage = new CarStorage();
        carStorage.setId(id.getAndAdd(1));
        carStorage.setCar(car);
        carStorage.setPrice(price);
        carStorage.setContacts(contacts);

        CARS.put(carStorage.getId(),carStorage);
        log.info("New car with id {} has been added for selling: {}", CARS.get(carStorage.getId()).getId(), CARS.get(carStorage.getId()));
        return carStorage;
    }


}
