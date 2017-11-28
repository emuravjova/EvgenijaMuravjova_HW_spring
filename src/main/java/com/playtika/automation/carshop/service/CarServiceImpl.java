package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.dao.entity.CarEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.SaleInfo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CarServiceImpl implements CarService {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Collection<CarSaleDetails> getAllCars() {
        List<OfferEntity> offers = entityManager.createQuery("from OfferEntity", OfferEntity.class).getResultList();
        log.info("All available cars with selling details has been returned");
        return offers
                .stream()
                .map(CarServiceImpl::getCarSaleDetailsFromOffer)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<CarSaleDetails> getCarDetailsById(long id) {
        List<OfferEntity> offers = entityManager.createQuery("from OfferEntity o where car_id=:id and deal_id is null", OfferEntity.class).setParameter("id", id).getResultList();
        Optional<CarSaleDetails> carDetails = offers.stream()
                .findFirst()
                .map(CarServiceImpl::getCarSaleDetailsFromOffer);
        log.info("Following car details has been returned by id {}: {}", id, carDetails);
        return carDetails;
    }

    @Transactional
    @Override
    public boolean deleteCarById(long id) {
        int count = entityManager.createQuery("delete from CarEntity where id=:id").setParameter("id", id).executeUpdate();
        if (count == 0) {
            log.warn("Car for sale with id = {} does not exist, cannot be removed", id);
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public long addCar(CarSaleDetails carDetails) {
        Optional<CarEntity> storedCarOptional = findCarByNumber(carDetails);
        if (!storedCarOptional.isPresent()) {
            CarEntity newCar = persistCar(carDetails.getCar());
            storeNewOffer(carDetails, newCar);
            log.info("New car {} with id {} has been added for selling, contacts: {}", carDetails.getCar(), newCar.getId(), carDetails.getSaleInfo());
            return newCar.getId();
        }

        CarEntity storedCar = storedCarOptional.get();
        boolean offerAlreadyExist = isOfferAlreadyExist(storedCar);
        if (offerAlreadyExist) {
            throw new IllegalArgumentException("Such car is already selling!");
        }

        storeNewOffer(carDetails, storedCar);
        log.info("Car {} with id {} has been added for selling one more time, contacts: {}", carDetails.getCar(), storedCar.getId(), carDetails.getSaleInfo());
        return storedCar.getId();

    }

    private boolean isOfferAlreadyExist(CarEntity storedCar) {
        List<OfferEntity> activeCarOffers = entityManager.createQuery("from OfferEntity where car=:car and deal_id is null", OfferEntity.class).setParameter("car", storedCar).getResultList();
        return !activeCarOffers.isEmpty();
    }

    private Optional<CarEntity> findCarByNumber(CarSaleDetails carDetails) {
        List<CarEntity> alreadyStoredCars = entityManager.createQuery("from CarEntity where number=:number", CarEntity.class).setParameter("number", carDetails.getCar().getNumber()).getResultList();
        return alreadyStoredCars.stream().findFirst();
    }

    private CarEntity persistCar(Car car) {
        CarEntity newCar = new CarEntity(car.getNumber(), car.getBrand(), car.getYear(), car.getColor());
        entityManager.persist(newCar);
        return newCar;
    }

    private static CarSaleDetails getCarSaleDetailsFromOffer(OfferEntity offer) {
        CarEntity entityCar = offer.getCar();
        Car car = new Car(entityCar.getNumber(), entityCar.getBrand(), entityCar.getYear(), entityCar.getColor());
        SaleInfo info = new SaleInfo(offer.getPrice(), offer.getSeller().getContacts());
        return new CarSaleDetails(entityCar.getId(), car, info);
    }

    private void storeNewOffer(CarSaleDetails carDetails, CarEntity car) {
        Optional<SellerEntity> storedSellerOptional = findSellerByContact(carDetails);
        if (!storedSellerOptional.isPresent()) {
            SellerEntity newSeller = persistSeller(carDetails.getSaleInfo());
            persistOffer(carDetails.getSaleInfo().getPrice(), car, newSeller);
        } else {
            SellerEntity storedSeller = storedSellerOptional.get();
            persistOffer(carDetails.getSaleInfo().getPrice(), car, storedSeller);
        }
    }

    private Optional<SellerEntity> findSellerByContact(CarSaleDetails carDetails) {
        List<SellerEntity> alreadyStoredSellers = entityManager.createQuery("from SellerEntity where contacts=:contacts", SellerEntity.class).setParameter("contacts", carDetails.getSaleInfo().getContacts()).getResultList();
        return alreadyStoredSellers.stream().findFirst();
    }

    private void persistOffer(int price, CarEntity car, SellerEntity seller) {
        OfferEntity newOffer = new OfferEntity(car, seller, price);
        entityManager.persist(newOffer);
    }

    private SellerEntity persistSeller(SaleInfo info) {
        SellerEntity newSeller = new SellerEntity("John doe", info.getContacts());
        entityManager.persist(newSeller);
        return newSeller;
    }
}
