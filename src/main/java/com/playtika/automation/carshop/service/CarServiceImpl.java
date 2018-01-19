package com.playtika.automation.carshop.service;

import com.playtika.automation.carshop.dao.CarDao;
import com.playtika.automation.carshop.dao.CustomerDao;
import com.playtika.automation.carshop.dao.DealDao;
import com.playtika.automation.carshop.dao.OfferDao;
import com.playtika.automation.carshop.dao.SellerDao;
import com.playtika.automation.carshop.dao.entity.CarEntity;
import com.playtika.automation.carshop.dao.entity.CustomerEntity;
import com.playtika.automation.carshop.dao.entity.DealEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
import com.playtika.automation.carshop.domain.Customer;
import com.playtika.automation.carshop.domain.SaleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final OfferDao offerRepo;
    private final CarDao carRepo;
    private final SellerDao sellerRepo;
    private final CustomerDao customerRepo;
    private final DealDao dealRepo;

    public CarServiceImpl(@Qualifier("jpaOfferDao") OfferDao offerRepo,
                          @Qualifier("jpaCarDao") CarDao carRepo,
                          @Qualifier("jpaSellerDao") SellerDao sellerRepo,
                          @Qualifier("jpaCustomerDao") CustomerDao customerRepo,
                          @Qualifier("jpaDealDao") DealDao dealRepo){
//    public CarServiceImpl(@Qualifier("couchbaseOfferDao") OfferDao offerRepo, @Qualifier("couchbaseCarDao") CarDao carRepo, @Qualifier("couchbaseSellerDao") SellerDao sellerRepo){
        this.offerRepo = offerRepo;
        this.carRepo = carRepo;
        this.sellerRepo = sellerRepo;
        this.customerRepo = customerRepo;
        this.dealRepo = dealRepo;
    }

    @Override
    public Collection<CarSaleDetails> getAllCars() {
        List<OfferEntity> offers = offerRepo.findByAcceptedDealIsNull();
        log.info("All available cars with selling details has been returned");
        return offers.stream()
                .map(CarServiceImpl::toCarSaleDetails)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<CarSaleDetails> getCarDetailsById(long id) {
        Optional<CarSaleDetails> carDetails = offerRepo.findByCarIdAndAcceptedDealIsNull(id)
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

    @Override
    public Optional<DealEntity> createDeal(Long id, int price, Customer customer) {
        Optional<OfferEntity> openOffer = findOpenOfferByCarId(id);
        boolean offerIsAbsent = !openOffer.isPresent();
        if (offerIsAbsent) {
            return Optional.empty();
        }
        Optional<CustomerEntity> storedCustomer = findCustomerByContact(customer.getContacts());
        CustomerEntity carCustomer = storedCustomer.orElseGet(() -> persistCustomer(customer));
        log.info("New deal for car {} has been added", openOffer.get().getCar());
        return Optional.of(persistDeal(openOffer.get(), price, carCustomer));
    }

    @Override
    public Optional<DealEntity> findTheBestDeal(Long id) {
        Optional<OfferEntity> offer = offerRepo.findByIdAndAcceptedDealIsNull(id);
        boolean offerIsClosed = !offer.isPresent();
        if (offerIsClosed){
            return Optional.empty();
        }
        List<DealEntity> deals = offer.get().getDeals();
        return findDealWithMaxPrice(deals);
    }

    @Override
    public boolean acceptDeal(long id) {
        Optional<OfferEntity> optionalOffer = offerRepo.findByDealsIdAndAcceptedDealIsNull(id);
        boolean offerIsClosed = !optionalOffer.isPresent();
        if (offerIsClosed){
            return false;
        }
        DealEntity deal = dealRepo.findOne(id);
        deal.setState(DealEntity.State.ACCEPTED);
        dealRepo.save(deal);
        log.info("Deal with id {} has been accepted", id);
        OfferEntity offer = optionalOffer.get();
        offer.setAcceptedDeal(deal);
        offerRepo.save(offer);
        log.info("Offer with id {} has been closed", offer.getId());
        return true;
    }

    @Override
    public void rejectDeal(long id) {
        DealEntity deal = dealRepo.findOne(id);
        deal.setState(DealEntity.State.REJECTED);
        dealRepo.save(deal);
        log.info("Deal with id {} has been rejected", id);
    }

    private Optional<DealEntity> findDealWithMaxPrice(List<DealEntity> deals) {
        return deals
                .stream()
                .max(Comparator.comparingInt(DealEntity::getPrice));
    }

    private Optional<OfferEntity> findOpenOfferByCarId(Long id) {
        return offerRepo.findByCarIdAndAcceptedDealIsNull(id)
                .stream()
                .findFirst();
    }

    private Optional<CustomerEntity> findCustomerByContact(String contacts) {
        return customerRepo.findFirstByContacts(contacts);
    }

    private CustomerEntity persistCustomer(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity(customer.getName(), customer.getContacts());
        return customerRepo.save(customerEntity);
    }

    private DealEntity persistDeal(OfferEntity offer, int price, CustomerEntity carCustomer) {
        DealEntity newDeal = new DealEntity(carCustomer, offer, price, DealEntity.State.ACTIVE);
        return dealRepo.save(newDeal);
    }

    private boolean isOfferAlreadyExist(CarEntity storedCar) {
        return !offerRepo.findByCarIdAndAcceptedDealIsNull(storedCar.getId()).isEmpty();
    }

    private Optional<CarEntity> findCarByNumber(CarSaleDetails carDetails) {
        return carRepo.findFirstByNumber(carDetails.getCar().getNumber());
    }

    private Optional<SellerEntity> findSellerByContact(CarSaleDetails carDetails) {
        return sellerRepo.findFirstByContacts(carDetails.getSaleInfo().getContacts());
    }

    private CarEntity persistCar(Car car) {
        CarEntity newCar = new CarEntity(car.getNumber(), car.getBrand(), car.getYear(), car.getColor());
        newCar = carRepo.save(newCar);
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
