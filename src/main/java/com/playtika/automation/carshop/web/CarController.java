package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/cars")
public class CarController {

    private static Map<Integer,Car> cars = new HashMap<>();

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Car> getAllCars(){
        return cars.values();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Car getCarById(@PathVariable("id") int id){
        return cars.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteCarById(@PathVariable("id") int id){
        cars.remove(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public int addCar(@RequestBody Car car){
        cars.put(car.getId(),car);
        log.info("New car with id {} has been added for selling: car model - {}, car price - {}, car owner - {}", cars.get(car.getId()).getId(), cars.get(car.getId()).getModel(),cars.get(car.getId()).getPrice(),cars.get(car.getId()).getOwner());
        return car.getId();
    }
}
