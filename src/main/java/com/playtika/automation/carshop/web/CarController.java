package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.dao.entity.DealEntity;
import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.Customer;
import com.playtika.automation.carshop.domain.SaleInfo;
import com.playtika.automation.carshop.service.CarService;
import com.playtika.automation.carshop.web.dto.CarId;
import com.playtika.automation.carshop.web.dto.DealInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@AllArgsConstructor
@Api(description = "Operations pertaining to products in Car store")
public class CarController {

    private final CarService carService;

    @GetMapping(value = "/cars")
    @ApiOperation(value = "View a list of available cars")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list")})
    public Collection<CarSaleDetails> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping(value = "/cars/{id}")
    @ApiOperation(value = "Search a sale details of car with an ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved sale details of car with an ID"),
            @ApiResponse(code = 404, message = "Car with an ID is not found")})
    public SaleInfo getCarDetailsById(@PathVariable("id") long id) {
        return carService.getCarDetailsById(id)
                .orElseThrow(() -> new CarNotFoundException("Unable to find car with id " + id))
                .getSaleInfo();
    }

    @DeleteMapping(value = "/cars/{id}")
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

    @PostMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add a car with sale details for sale")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car with an ID was successfully added"),
            @ApiResponse(code = 400, message = "Not all required parameters are received"),
            @ApiResponse(code = 500, message = "Such car is already on sale")})
    public CarId addCar(
            @Valid @RequestBody Car car,
            @ApiParam(name = "price", required = true, defaultValue = "20000")
            @NotEmpty @RequestParam("price") int price,
            @ApiParam(name = "contacts", required = true, defaultValue = "0987656789")
            @NotEmpty @RequestParam("contacts") String contacts) {
        CarSaleDetails carToAdd = new CarSaleDetails();
        carToAdd.setCar(car);
        carToAdd.setSaleInfo(new SaleInfo(price, contacts));
        return new CarId(carService.addCar(carToAdd));
    }

    @PostMapping(value = "/deal", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create new deal")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deal with an ID was successfully created"),
            @ApiResponse(code = 404, message = "Such car is not on sale or no such car at all")})
    public DealInfo createDeal(
            @Valid @RequestBody Customer customer,
            @ApiParam(name = "price", required = true, defaultValue = "20000")
            @NotEmpty @RequestParam("price") int price,
            @ApiParam(name = "carId", required = true, defaultValue = "1")
            @NotEmpty @RequestParam("carId") Long id) {
        DealEntity deal = carService.createDeal(id, price, customer)
                .orElseThrow(() -> new CarOnSaleNotFoundException("Such car is not on sale or no such car at all!"));
        return new DealInfo(deal.getId(), deal.getOffer().getId());
    }

    @GetMapping(value = "/offer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find the best deal by offer id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The best deal is successfully found for offer"),
            @ApiResponse(code = 404, message = "No best deal found for open offer")})
    public DealInfo findTheBestDeal(@PathVariable("id") long id) {
        DealEntity deal = carService.findTheBestDeal(id)
                .orElseThrow(() -> new NoBestDealForOpenOfferException("Such offer is already closed or no deal found for open offer"));
        return new DealInfo(deal.getId(), deal.getOffer().getId());
    }

    @PutMapping(value = "/acceptDeal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Accept deal")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The deal is successfully accepted"),
            @ApiResponse(code = 409, message = "Deal cannot be accepted for closed offer")})
    public ResponseEntity<Void> acceptDeal(@PathVariable("id") long id) {
        if (carService.acceptDeal(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/rejectDeal/{id}")
    @ApiOperation(value = "Reject deal")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The deal is successfully rejected")})
    public void rejectDeal(@PathVariable("id") long id) {
        carService.rejectDeal(id);
    }
}
