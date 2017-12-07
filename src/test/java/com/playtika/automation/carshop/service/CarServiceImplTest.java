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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    private CarService carService;

    @Mock
    private OfferDao offerRepo;
    @Mock
    private CarDao carRepo;
    @Mock
    private SellerDao sellerRepo;

    @Before
    public void init() {
        carService = new CarServiceImpl(offerRepo, carRepo, sellerRepo);
    }

    @Test
    public void ifNoCarsPresentShouldReturnEmptyResult() {
        assertThat(carService.getAllCars(), is(empty()));
    }

    @Test
    public void shouldReturnAllAvailableCars() {
        CarEntity car = getCarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = getSellerEntity(1L, "Den", "0501234567");
        List<OfferEntity> expectedOffers = new ArrayList<>();
        addOfferEntities(expectedOffers, 1L, car, seller, 12000);
        CarSaleDetails availableCarToSale = getCarSaleDetails(car, seller, 12000);

        when(offerRepo.findByDealIsNull()).thenReturn(expectedOffers);
        Collection<CarSaleDetails> availableCars = carService.getAllCars();

        assertThat(availableCars, hasItem(availableCarToSale));
        assertThat(availableCars, iterableWithSize(1));
    }

    @Test
    public void shouldGetCarDetailsById() {
        CarEntity car = getCarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = getSellerEntity(1L, "Den", "0501234567");
        List<OfferEntity> expectedOffers = new ArrayList<>();
        addOfferEntities(expectedOffers, 2L, car, seller, 12000);
        CarSaleDetails availableCarToSale = getCarSaleDetails(car, seller, 12000);

        when(offerRepo.findByCarIdAndDealIsNull(1L)).thenReturn(expectedOffers);

        assertThat(carService.getCarDetailsById(1L), equalTo(java.util.Optional.of(availableCarToSale)));
    }

    @Test
    public void shouldReturnEmptyOptionalWhenNoCarFound() {
        assertTrue(!carService.getCarDetailsById(100500).isPresent());
    }

    @Test
    public void shouldReturnTrueWhenCarIsDeletedById() {
        when(carRepo.deleteById(1L)).thenReturn(1);
        assertTrue(carService.deleteCarById(1L));
    }

    @Test
    public void shouldReturnFalseWhenCarNotDeletedById() {
        when(carRepo.deleteById(1L)).thenReturn(0);
        assertFalse(carService.deleteCarById(1L));
    }


    @Test
    public void newCarWithNewSellerShouldBeStored() {
        CarEntity carBeforeStore = new CarEntity("AS123", "BMW", 2007, "blue");
        CarEntity carAfterStore = getCarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = new SellerEntity("John doe", "0501234567");
        OfferEntity offer = new OfferEntity(carAfterStore, seller, 12000);
        CarSaleDetails carToStore = getCarSaleDetails(carBeforeStore, seller, offer.getPrice());

        when(carRepo.findFirstByNumber(carBeforeStore.getNumber())).thenReturn(Optional.empty());
        when(sellerRepo.findFirstByContacts(seller.getContacts())).thenReturn(Optional.empty());
        when(offerRepo.findByCarIdAndDealIsNull(carAfterStore.getId())).thenReturn(Collections.EMPTY_LIST);
        when(carRepo.save(carBeforeStore)).thenReturn(carAfterStore);
        when(sellerRepo.save(seller)).thenReturn(seller);
        when(offerRepo.save(offer)).thenReturn(offer);

        long id = carService.addCar(carToStore);

        assertThat(id, greaterThan(0L));
        verify(carRepo, times(1)).save(carBeforeStore);
        verify(sellerRepo, times(1)).save(seller);
        verify(offerRepo, times(1)).save(offer);

    }

    @Test
    public void newCarWithExistedSellerShouldBeStored() {
        CarEntity carBeforeStore = new CarEntity("AS123", "BMW", 2007, "blue");
        CarEntity carAfterStore = getCarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = new SellerEntity("John doe", "0501234567");
        OfferEntity offer = new OfferEntity(carAfterStore, seller, 12000);
        CarSaleDetails carToStore = getCarSaleDetails(carBeforeStore, seller, offer.getPrice());

        when(carRepo.findFirstByNumber(carBeforeStore.getNumber())).thenReturn(Optional.empty());
        when(sellerRepo.findFirstByContacts(seller.getContacts())).thenReturn(Optional.of(seller));
        when(offerRepo.findByCarIdAndDealIsNull(carAfterStore.getId())).thenReturn(Collections.EMPTY_LIST);
        when(carRepo.save(carBeforeStore)).thenReturn(carAfterStore);
        when(offerRepo.save(offer)).thenReturn(offer);

        long id = carService.addCar(carToStore);

        assertThat(id, greaterThan(0L));
        verify(carRepo, times(1)).save(carBeforeStore);
        verify(sellerRepo, never()).save(seller);
        verify(offerRepo, times(1)).save(offer);
    }

    @Test
    public void existedCarShouldBeStoredWhenNoActiveOffer() {
        CarEntity carBeforeStore = new CarEntity("AS123", "BMW", 2007, "blue");
        CarEntity carAfterStore = getCarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = new SellerEntity("John doe", "0501234567");
        OfferEntity offer = new OfferEntity(carAfterStore, seller, 12000);
        CarSaleDetails carToStore = getCarSaleDetails(carAfterStore, seller, offer.getPrice());

        when(carRepo.findFirstByNumber(carAfterStore.getNumber())).thenReturn(Optional.of(carAfterStore));
        when(sellerRepo.findFirstByContacts(seller.getContacts())).thenReturn(Optional.of(seller));
        when(offerRepo.findByCarIdAndDealIsNull(carAfterStore.getId())).thenReturn(Collections.EMPTY_LIST);
        when(offerRepo.save(offer)).thenReturn(offer);

        long id = carService.addCar(carToStore);

        assertThat(id, greaterThan(0L));
        verify(carRepo, never()).save(carBeforeStore);
        verify(sellerRepo, never()).save(seller);
        verify(offerRepo, times(1)).save(offer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void existedCarShouldNotBeStoredWhenActiveOfferExists() {
        CarEntity carBeforeStore = new CarEntity("AS123", "BMW", 2007, "blue");
        CarEntity carAfterStore = getCarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = new SellerEntity("John doe", "0501234567");
        OfferEntity offer = getOfferEntity(1L, carAfterStore, seller, 12000);
        List<OfferEntity> offers = new ArrayList<>();
        offers.add(offer);
        CarSaleDetails carToStore = getCarSaleDetails(carAfterStore, seller, offer.getPrice());

        when(carRepo.findFirstByNumber(carAfterStore.getNumber())).thenReturn(Optional.of(carAfterStore));
        when(sellerRepo.findFirstByContacts(seller.getContacts())).thenReturn(Optional.of(seller));
        when(offerRepo.findByCarIdAndDealIsNull(carAfterStore.getId())).thenReturn(offers);

        carService.addCar(carToStore);

        verify(carRepo, never()).save(carBeforeStore);
        verify(sellerRepo, never()).save(seller);
        verify(offerRepo, never()).save(offer);
    }

    private List<OfferEntity> addOfferEntities(List<OfferEntity> expectedOffers, long id, CarEntity car, SellerEntity seller, int price) {
        expectedOffers.add(getOfferEntity(id, car, seller, price));
        return expectedOffers;
    }

    private CarSaleDetails getCarSaleDetails(CarEntity car, SellerEntity seller, int price) {
        CarSaleDetails getCarWithInfo = new CarSaleDetails();
        getCarWithInfo.setCar(new Car(car.getNumber(),car.getBrand(),car.getYear(),car.getColor()));
        getCarWithInfo.setSaleInfo(new SaleInfo(price,seller.getContacts()));
        return getCarWithInfo;
    }

    private SellerEntity getSellerEntity(long id, String name, String contacts) {
        SellerEntity seller = new SellerEntity(name, contacts);
        seller.setId(id);
        return seller;
    }

    private CarEntity getCarEntity(long id, String number, String brand, int year, String color) {
        CarEntity car = new CarEntity(number, brand, year, color);
        car.setId(id);
        return car;
    }

    private OfferEntity getOfferEntity(long id, CarEntity car, SellerEntity seller, int price) {
        OfferEntity newOffer = new OfferEntity(car, seller, price);
        newOffer.setId(id);
        return newOffer;
    }
}