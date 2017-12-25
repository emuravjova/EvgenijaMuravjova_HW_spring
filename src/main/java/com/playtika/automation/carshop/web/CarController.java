package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.SaleInfo;
import com.playtika.automation.carshop.service.CarService;
import com.playtika.automation.carshop.web.dto.CarId;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;

@RestController
@AllArgsConstructor
@RequestMapping("/cars")
@Api(description="Operations pertaining to products in Car store")
public class CarController {

    private final CarService carService;

    @GetMapping
    @ApiOperation(value = "View a list of available cars")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list")})
    public Collection<CarSaleDetails> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Search a sale details of car with an ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved sale details of car with an ID"),
            @ApiResponse(code = 404, message = "Car with an ID is not found")})
    public SaleInfo getCarDetailsById(@PathVariable("id") long id) {
        return carService.getCarDetailsById(id)
                .orElseThrow(() -> new CarNotFoundException("Unable to find car with id " + id))
                .getSaleInfo();
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete a car with an ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car with an ID was successfully deleted"),
            @ApiResponse(code = 204, message = "Car with an ID is already deleted")})
    public ResponseEntity<Void> deleteCarById(@PathVariable("id") long id) {
       if (carService.deleteCarById(id)) {
           return new ResponseEntity<>(HttpStatus.OK);
       } else {
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add a car with sale details for sale")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car with an ID was successfully added"),
            @ApiResponse(code = 400, message = "Not all required parameters are received"),
            @ApiResponse(code = 500, message = "Such car is already on sale")})
    public CarId addCar(
            @Valid @RequestBody Car car,
            @ApiParam(name = "Price", required = true, defaultValue = "20000")
            @NotEmpty @RequestParam("price") int price,
            @ApiParam(name = "Contacts", required = true, defaultValue = "0987656789")
            @NotEmpty @RequestParam("contacts") String contacts) {
        CarSaleDetails carToAdd = new CarSaleDetails();
        carToAdd.setCar(car);
        carToAdd.setSaleInfo(new SaleInfo(price, contacts));
        return new CarId(carService.addCar(carToAdd));
    }

}
