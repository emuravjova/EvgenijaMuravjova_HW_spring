package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.dao.JpaCarDao;
import com.playtika.automation.carshop.dao.JpaCustomerDao;
import com.playtika.automation.carshop.dao.JpaDealDao;
import com.playtika.automation.carshop.dao.JpaOfferDao;
import com.playtika.automation.carshop.dao.JpaSellerDao;
import com.playtika.automation.carshop.dao.entity.*;
import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.Customer;
import com.playtika.automation.carshop.domain.SaleInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;

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
    @Qualifier("jpaOfferDao")
    private JpaOfferDao offerRepo;
    @Mock
    @Qualifier("jpaCarDao")
    private JpaCarDao carRepo;
    @Mock
    @Qualifier("jpaSellerDao")
    private JpaSellerDao sellerRepo;
    @Mock
    @Qualifier("jpaCustomerDao")
    private JpaCustomerDao customerRepo;
    @Mock
    @Qualifier("jpaDealDao")
    private JpaDealDao dealRepo;

    @Before
    public void init() {
        carService = new CarServiceImpl(offerRepo, carRepo, sellerRepo, customerRepo, dealRepo);
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

        when(offerRepo.findByAcceptedDealIsNull()).thenReturn(expectedOffers);
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

        when(offerRepo.findByCarIdAndAcceptedDealIsNull(1L)).thenReturn(expectedOffers);

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
        when(offerRepo.findByCarIdAndAcceptedDealIsNull(carAfterStore.getId())).thenReturn(Collections.EMPTY_LIST);
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
        when(offerRepo.findByCarIdAndAcceptedDealIsNull(carAfterStore.getId())).thenReturn(Collections.EMPTY_LIST);
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
        when(offerRepo.findByCarIdAndAcceptedDealIsNull(carAfterStore.getId())).thenReturn(Collections.EMPTY_LIST);
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
        when(offerRepo.findByCarIdAndAcceptedDealIsNull(carAfterStore.getId())).thenReturn(offers);

        carService.addCar(carToStore);

        verify(carRepo, never()).save(carBeforeStore);
        verify(sellerRepo, never()).save(seller);
        verify(offerRepo, never()).save(offer);
    }

    @Test
    public void dealShouldNotBeCreatedForCarThatIsNotOnSale() {
        when(offerRepo.findByCarIdAndAcceptedDealIsNull(1L)).thenReturn(Collections.EMPTY_LIST);

        carService.createDeal(1L, 100500, new Customer("Den", "0969876543"));

        verify(customerRepo, never()).findFirstByContacts("0969876543");
        verify(customerRepo, never()).save(new CustomerEntity("Den", "0969876543"));
        verify(dealRepo, never()).save(Matchers.isA(DealEntity.class));
    }

    @Test
    public void dealShouldBeCreatedWhenCustomerIsPresent() {
        OfferEntity offer = createOffer();
        List<OfferEntity> offers = new ArrayList<>();
        offers.add(offer);
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity dealToStore = new DealEntity(customer, offer, 100500, DealEntity.State.ACTIVE);
        DealEntity newDeal = new DealEntity(1L, customer, offer, 100500, DealEntity.State.ACTIVE);

        when(offerRepo.findByCarIdAndAcceptedDealIsNull(offer.getCar().getId())).thenReturn(offers);
        when(customerRepo.findFirstByContacts(customer.getContacts())).thenReturn(Optional.of(customer));
        when(dealRepo.save(dealToStore)).thenReturn(newDeal);

        Optional<DealEntity> actualDeal = carService.createDeal(1L, 100500, new Customer("Den", "0969876543"));

        assertThat(actualDeal.get().getId(), greaterThan(0L));
        verify(dealRepo, times(1)).save(dealToStore);
        verify(customerRepo, times(1)).findFirstByContacts("0969876543");
        verify(customerRepo, never()).save(new CustomerEntity("Den", "0969876543"));
    }

    @Test
    public void dealShouldBeCreatedWhenCustomerIsAbsent() {
        OfferEntity offer = createOffer();
        List<OfferEntity> offers = new ArrayList<>();
        offers.add(offer);
        CustomerEntity customerToStore = new CustomerEntity("Den", "0969876543");
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity dealToStore = new DealEntity(customer, offer, 100500, DealEntity.State.ACTIVE);
        DealEntity newDeal = new DealEntity(1L, customer, offer, 100500, DealEntity.State.ACTIVE);

        when(offerRepo.findByCarIdAndAcceptedDealIsNull(offer.getCar().getId())).thenReturn(offers);
        when(customerRepo.findFirstByContacts(customerToStore.getContacts())).thenReturn(Optional.empty());
        when(customerRepo.save(customerToStore)).thenReturn(customer);
        when(dealRepo.save(dealToStore)).thenReturn(newDeal);

        Optional<DealEntity> actualDeal = carService.createDeal(1L, 100500, new Customer("Den", "0969876543"));

        assertThat(actualDeal.get().getId(), greaterThan(0L));
        verify(dealRepo, times(1)).save(dealToStore);
        verify(customerRepo, times(1)).findFirstByContacts("0969876543");
        verify(customerRepo, times(1)).save(new CustomerEntity("Den", "0969876543"));
    }

    @Test
    public void bestDealShouldBeReturnedForOpenOffer() {
        OfferEntity offer = createOffer();
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity deal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.ACTIVE);
        DealEntity bestDeal = new DealEntity(2L, customer, offer, 30000, DealEntity.State.ACTIVE);
        List<DealEntity> deals = new ArrayList<>();
        deals.add(deal);
        deals.add(bestDeal);

        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.of(offer));
        when(dealRepo.findByOfferId(offer.getId())).thenReturn(deals);

        assertThat(carService.findTheBestDeal(1L).get().getPrice(), equalTo(30000));
    }

    @Test
    public void bestDealShouldBeCalculatedAmongActiveDeals() {
        OfferEntity offer = createOffer();
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity activeDeal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.ACTIVE);
        DealEntity rejectedDeal = new DealEntity(2L, customer, offer, 30000, DealEntity.State.REJECTED);
        List<DealEntity> deals = new ArrayList<>();
        deals.add(activeDeal);
        deals.add(rejectedDeal);

        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.of(offer));
        when(dealRepo.findByOfferId(offer.getId())).thenReturn(deals);

        assertThat(carService.findTheBestDeal(1L).get().getPrice(), equalTo(20000));
    }

    @Test
    public void bestDealShouldNotBeCalculatedForClosedOffer() {
        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.empty());
        assertTrue(!carService.findTheBestDeal(1L).isPresent());
    }

    @Test
    public void bestDealShouldNotBeReturnedWhenNoDealsAtAllForOpenOffer() {
        OfferEntity offer = createOffer();

        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.of(offer));
        assertTrue(!carService.findTheBestDeal(1L).isPresent());
    }

    @Test
    public void bestDealShouldNotBeReturnedWhenNoActiveDealsForOpenOffer() {
        OfferEntity offer = createOffer();
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity deal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.REJECTED);

        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.of(offer));
        assertTrue(!carService.findTheBestDeal(1L).isPresent());
    }

    @Test
    public void dealShouldBeRejectedForOpenOffer() {
        OfferEntity offer = createOffer();
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity deal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.ACTIVE);
        DealEntity rejectedDeal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.REJECTED);

        when(dealRepo.findOne(1L)).thenReturn(deal);
        when(dealRepo.save(rejectedDeal)).thenReturn(rejectedDeal);
        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.of(offer));
        carService.rejectDeal(1L);
        assertFalse(carService.findTheBestDeal(1L).isPresent());
    }

    @Test
    public void dealShouldBeAcceptedForOpenOffer() {
        OfferEntity offer = createOffer();
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity deal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.ACTIVE);
        DealEntity acceptedDeal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.ACCEPTED);

        when(dealRepo.findOne(1L)).thenReturn(deal);
        when(dealRepo.save(acceptedDeal)).thenReturn(acceptedDeal);
        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.of(offer));

        assertTrue(carService.acceptDeal(1L));
        assertThat(offer.getAcceptedDeal().getState(), equalTo(DealEntity.State.ACCEPTED));
    }

    @Test
    public void dealShouldNotBeAcceptedForClosedOffer() {
        OfferEntity offer = createOffer();
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity deal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.ACCEPTED);

        when(dealRepo.findOne(1L)).thenReturn(deal);
        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.empty());

        assertFalse(carService.acceptDeal(1L));
    }

    @Test
    public void offerShouldBeClosedWhenDealIsAccepted() {
        OfferEntity offer = createOffer();
        CustomerEntity customer = new CustomerEntity(1L, "Den", "0969876543");
        DealEntity deal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.ACTIVE);
        DealEntity acceptedDeal = new DealEntity(1L, customer, offer, 20000, DealEntity.State.ACCEPTED);

        when(dealRepo.findOne(1L)).thenReturn(deal);
        when(dealRepo.save(acceptedDeal)).thenReturn(acceptedDeal);
        when(offerRepo.findByIdAndAcceptedDealIsNull(1L)).thenReturn(Optional.of(offer));

        assertTrue(carService.acceptDeal(1L));
        assertThat(offer.getAcceptedDeal().getId(), equalTo(acceptedDeal.getId()));
    }


    private OfferEntity createOffer() {
        CarEntity car = getCarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = new SellerEntity("John doe", "0501234567");
        return getOfferEntity(1L, car, seller, 12000);
    }

    private List<OfferEntity> addOfferEntities(List<OfferEntity> expectedOffers, long id, CarEntity car, SellerEntity seller, int price) {
        expectedOffers.add(getOfferEntity(id, car, seller, price));
        return expectedOffers;
    }

    private CarSaleDetails getCarSaleDetails(CarEntity car, SellerEntity seller, int price) {
        CarSaleDetails getCarWithInfo = new CarSaleDetails();
        getCarWithInfo.setCar(new Car(car.getNumber(), car.getBrand(), car.getYear(), car.getColor()));
        getCarWithInfo.setSaleInfo(new SaleInfo(price, seller.getContacts()));
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