package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.service.CarService;
import com.playtika.automation.carshop.service.CarServiceImpl;
import com.playtika.automation.carshop.web.dto.SaleInfo;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @GetMapping
    public Collection<CarSaleDetails> getAllCars(){
        return carService.getAllCars();
    }

    @GetMapping(value = "/{id}")
    public SaleInfo getCarDetailsById(@PathVariable("id") long id){
        CarSaleDetails carDetails = carService.getCarDetailsById(id);
        if (carDetails == null) {
            throw new CarNotFoundException("Unable to find car with id " + id);
        } else {return new SaleInfo(carDetails.getPrice(), carDetails.getContacts());}
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCarById(@PathVariable("id") long id){
        carService.deleteCarById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CarSaleDetails addCar(
            @Valid @RequestBody Car car,
            @NotEmpty @RequestParam("price") int price,
            @NotEmpty @RequestParam("contacts") String contacts)
    {
        CarSaleDetails carToAdd = new CarSaleDetails();
        carToAdd.setCar(car);
        carToAdd.setPrice(price);
        carToAdd.setContacts(contacts);
        return carService.addCar(carToAdd);
    }

}
