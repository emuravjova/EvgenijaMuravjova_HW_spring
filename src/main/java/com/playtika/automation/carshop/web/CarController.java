package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.CarStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/cars")
public class CarController {

    private static Map<Integer,CarStorage> cars = new HashMap<>();

    @RequestMapping(method = RequestMethod.GET)
    public Collection<CarStorage> getAllCars(){
        log.info("All available cars with selling details has been returned");
        return cars.values();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getCarDetailsById(@PathVariable("id") int id){
        if (cars.containsKey(id)) {
            log.info("Car selling details with id = {} has been successfully returned", id);
            return new ResponseEntity<>(cars.get(id).getCarSaleDetails(), HttpStatus.OK);
        }
        else {
            log.warn("Car with id = {} not found, sale details cannot be returned", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCarById(@PathVariable("id") int id){
        if (cars.containsKey(id)) {
            cars.remove(id);
            log.info("Car with id = {} has been successfully removed", id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            log.warn("Car with id = {} not found, cannot be removed", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity addCar(
            @RequestBody Car car,
            @RequestParam("price") double price,
            @RequestParam("contacts") String contacts)
    {
        CarSaleDetails carSaleDetails = new CarSaleDetails();
        carSaleDetails.setPrice(price);
        carSaleDetails.setContacts(contacts);

        CarStorage carStorage = new CarStorage();
        carStorage.setCar(car);
        carStorage.setCarSaleDetails(carSaleDetails);

        cars.put(carStorage.getId(),carStorage);
        log.info("New car with id {} has been added for selling: {}", cars.get(carStorage.getId()).getId(), cars.get(carStorage.getId()));
        return new ResponseEntity<>(carStorage, HttpStatus.OK);
    }


}
