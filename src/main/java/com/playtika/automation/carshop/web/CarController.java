package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.SaleInfo;
import com.playtika.automation.carshop.service.CarService;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

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
        return carService.getCarDetailsById(id)
                .orElseThrow(() -> new CarNotFoundException("Unable to find car with id " + id))
                .getSaleInfo();
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
        carToAdd.setSaleInfo(new SaleInfo(price, contacts));
        return carService.addCar(carToAdd);
    }

}
