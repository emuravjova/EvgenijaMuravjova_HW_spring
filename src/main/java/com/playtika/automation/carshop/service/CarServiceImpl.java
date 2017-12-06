package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.dao.CarDao;
import com.playtika.automation.carshop.dao.OfferDao;
import com.playtika.automation.carshop.dao.SellerDao;
import com.playtika.automation.carshop.dao.entity.CarEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.SaleInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final OfferDao offerRepo;
    private final CarDao carRepo;
    private final SellerDao sellerRepo;

    @Override
    public Collection<CarSaleDetails> getAllCars() {
        List<OfferEntity> offers = offerRepo.findByDeal(null);
        log.info("All available cars with selling details has been returned");
        return offers.stream()
                .map(CarServiceImpl::toCarSaleDetails)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<CarSaleDetails> getCarDetailsById(long id) {
        Optional<CarSaleDetails> carDetails = offerRepo.findByCarIdAndDeal(id, null)
                .stream()
                .findFirst()
                .map(CarServiceImpl::toCarSaleDetails);
        log.info("Following car details has been returned by id {}: {}", id, carDetails);
        return carDetails;
    }

    @Override
    public boolean deleteCarById(long id) {
        int count = carRepo.deleteById(id);
        if (count == 0) {
            log.warn("Car for sale with id = {} does not exist, cannot be removed", id);
            return false;
        }
        return true;
    }

    @Override
    public long addCar(CarSaleDetails carDetails) {
        Optional<CarEntity> storedCarOptional = findCarByNumber(carDetails);
        CarEntity car = storedCarOptional.orElseGet(() -> persistCar(carDetails.getCar()));
       boolean offerAlreadyExist = storedCarOptional.isPresent() && isOfferAlreadyExist(car);
        if (offerAlreadyExist) {
            throw new IllegalArgumentException("Such car is already selling!");
        }
        Optional<SellerEntity> storedSellerOptional = findSellerByContact(carDetails);
        SellerEntity seller = storedSellerOptional.orElseGet(() -> persistSeller(carDetails.getSaleInfo()));
        persistOffer(carDetails.getSaleInfo().getPrice(), car, seller);
        log.info("Car {} with id {} has been added for selling, contacts: {}", carDetails.getCar(), car.getId(), carDetails.getSaleInfo());
        return car.getId();
    }

    private boolean isOfferAlreadyExist(CarEntity storedCar) {
        OfferEntity offer = new OfferEntity();
        offer.setCar(storedCar);
        return !offerRepo.exists(Example.of(offer));
    }

    private Optional<CarEntity> findCarByNumber(CarSaleDetails carDetails) {
        return carRepo.findFirstByNumber(carDetails.getCar().getNumber());
    }

    private Optional<SellerEntity> findSellerByContact(CarSaleDetails carDetails) {
        return sellerRepo.findFirstByContacts(carDetails.getSaleInfo().getContacts());
    }

    private CarEntity persistCar(Car car) {
        CarEntity newCar = new CarEntity(car.getNumber(), car.getBrand(), car.getYear(), car.getColor());
        carRepo.save(newCar);
        log.info("New car {} with id {} has been added", car, newCar.getId());
        return newCar;
    }

    private void persistOffer(int price, CarEntity car, SellerEntity seller) {
        OfferEntity newOffer = new OfferEntity(car, seller, price);
        offerRepo.save(newOffer);
    }

    private SellerEntity persistSeller(SaleInfo info) {
        SellerEntity newSeller = new SellerEntity("John doe", info.getContacts());
        sellerRepo.save(newSeller);
        return newSeller;
    }

    private static CarSaleDetails toCarSaleDetails(OfferEntity offer) {
        CarEntity entityCar = offer.getCar();
        Car car = new Car(entityCar.getNumber(), entityCar.getBrand(), entityCar.getYear(), entityCar.getColor());
        SaleInfo info = new SaleInfo(offer.getPrice(), offer.getSeller().getContacts());
        return new CarSaleDetails(entityCar.getId(), car, info);
    }
}
