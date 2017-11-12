package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.service.CarServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private final CarServiceImpl carService;

    @GetMapping
    public Collection<CarSaleDetails> getAllCars(){
        return carService.getAllCars();
    }

    @GetMapping(value = "/{id}")
    public Map<String,Object> getCarDetailsById(@PathVariable("id") long id){
        return carService.getCarDetailsById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCarById(@PathVariable("id") long id){
        carService.deleteCarById(id);
    }

    @PostMapping
    public CarSaleDetails addCar(
            @Valid @RequestBody Car car,
            @RequestParam("price") double price,
            @RequestParam("contacts") String contacts)
    {
        return carService.addCar(car, price, contacts);
    }


}
