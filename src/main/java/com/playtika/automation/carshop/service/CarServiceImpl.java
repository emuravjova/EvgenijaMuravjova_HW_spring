package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.dao.entity.CarEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.SaleInfo;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Service
public class CarServiceImpl implements CarService {

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
    }

    @Override
    public Optional<CarSaleDetails> getCarDetailsById(long id) {
        List<OfferEntity> offers = entityManager.createQuery("from OfferEntity o where car_id=:id and deal_id is null", OfferEntity.class).setParameter("id", id).getResultList();
        if (offers.isEmpty()){
            return Optional.empty();
        }
        CarSaleDetails carDetails = getCarSaleDetailsFromOffer((OfferEntity) offers.get(0));
        log.info("Following car details has been returned by id {}: {}", id, carDetails);
        return Optional.of(carDetails);
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
        List<CarEntity> alreadyStoredCar = entityManager.createQuery("from CarEntity where number=:number", CarEntity.class).setParameter("number", carDetails.getCar().getNumber()).getResultList();
        if (alreadyStoredCar.isEmpty()){
            CarEntity newCar = new CarEntity(carDetails.getCar().getNumber(), carDetails.getCar().getBrand(), carDetails.getCar().getYear(), carDetails.getCar().getColor());
            entityManager.persist(newCar);
            storeNewOffer(carDetails, newCar);
            log.info("New car {} with id {} has been added for selling, contacts: {}", carDetails.getCar(), newCar.getId(), carDetails.getSaleInfo());
            return newCar.getId();
        } else {
            List<OfferEntity> activeCarOffers = entityManager.createQuery("from OfferEntity where car=:car and deal_id is null", OfferEntity.class).setParameter("car", alreadyStoredCar.get(0)).getResultList();
            if (activeCarOffers.isEmpty()){
                storeNewOffer(carDetails, alreadyStoredCar.get(0));
                log.info("Car {} with id {} has been added for selling one more time, contacts: {}", carDetails.getCar(), alreadyStoredCar.get(0).getId(), carDetails.getSaleInfo());
                return alreadyStoredCar.get(0).getId();
            }
        }
        throw new IllegalArgumentException("Such car is already selling!");
    }

    private static CarSaleDetails getCarSaleDetailsFromOffer(OfferEntity offer){
        return new CarSaleDetails(offer.getId(), new Car(offer.getCar().getNumber(), offer.getCar().getBrand(), offer.getCar().getYear(), offer.getCar().getColor()), new SaleInfo(offer.getPrice(), offer.getSeller().getContacts()));
    }

    private void storeNewOffer(CarSaleDetails carDetails, CarEntity car){
        SellerEntity newSeller = new SellerEntity("John doe", carDetails.getSaleInfo().getContacts());
        entityManager.persist(newSeller);
        OfferEntity newOffer = new OfferEntity(car, newSeller, carDetails.getSaleInfo().getPrice(), null);
        entityManager.persist(newOffer);
    }
}
