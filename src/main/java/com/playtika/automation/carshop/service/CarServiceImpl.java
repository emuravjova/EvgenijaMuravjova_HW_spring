package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.dao.entity.CarEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.SaleInfo;
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
@Service
public class CarServiceImpl implements CarService {

//    private final Map<Long,CarSaleDetails> CARS = new ConcurrentHashMap<>();
//
//    private final AtomicLong ID = new AtomicLong(1);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<CarSaleDetails> getAllCars() {
        List<OfferEntity> offers = entityManager.createQuery("from OfferEntity", OfferEntity.class).getResultList();
        log.info("All available cars with selling details has been returned");
        return offers
                .stream()
                .map(CarServiceImpl::getCarSaleDetailsFromOffer)
                .collect(Collectors.toCollection(ArrayList::new));
//        return CARS.values();
    }

    @Override
    public Optional<CarSaleDetails> getCarDetailsById(long id) {
        OfferEntity offer = entityManager.find(OfferEntity.class, id);
        if (offer == null){
            return Optional.empty();
        } else {
            CarSaleDetails carDetails = new CarSaleDetails(id, new Car(offer.getCar().getBrand(), offer.getCar().getNumber()), new SaleInfo(offer.getPrice(), offer.getSeller().getContacts()));
            //       Optional<CarSaleDetails> carDetails = Optional.ofNullable(CARS.get(id));
            log.info("Following car details has been returned by id {}: {}", id, carDetails);
            return Optional.of(carDetails);
        }
    }

    @Transactional
    @Override
    public boolean deleteCarById(long id) {
//        if (CARS.remove(id) == null) {
//            log.warn("Car with id = {} does not exist, cannot be removed", id);
//            return false;
//        }
//        log.info("Car with id = {} has been successfully removed", id);
//        return true;
        OfferEntity offer = entityManager.find(OfferEntity.class, id);
        if (offer == null) {
            log.warn("Car for sale with id = {} does not exist, cannot be removed", id);
            return false;
        }
        entityManager.remove(offer);
        return true;
    }

    @Transactional
    @Override
    public long addCar(CarSaleDetails carToAdd) {
//        long id = ID.getAndAdd(1);
//        carToAdd.setId(id);
//        CARS.put(carToAdd.getId(), carToAdd);
        SellerEntity seller = new SellerEntity("John doe", carToAdd.getSaleInfo().getContacts());
        CarEntity car = new CarEntity(carToAdd.getCar().getModel(), carToAdd.getCar().getName(), 2017, "green");
        OfferEntity offer = new OfferEntity(car, seller, carToAdd.getSaleInfo().getPrice(), null);
        entityManager.persist(car);
        entityManager.persist(seller);
        entityManager.persist(offer);
        log.info("New car {} with id {} has been added for selling, contacts: {}", carToAdd.getCar(), offer.getId(), carToAdd.getSaleInfo());
        return offer.getId();
    }

    private static CarSaleDetails getCarSaleDetailsFromOffer(OfferEntity offer){
        return new CarSaleDetails(offer.getId(), new Car(offer.getCar().getBrand(),offer.getCar().getNumber()), new SaleInfo(offer.getPrice(), offer.getSeller().getContacts()));
    }
}
