package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.Customer;
import com.playtika.automation.carshop.web.dto.CarId;
import com.playtika.automation.carshop.web.dto.DealInfo;
import io.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CarControllerSystemTest {

    @Test
    public void shouldAddCar() throws Exception {
        Car car = new Car("AM123", "BMW", 2007, "blue");
        given()
                .contentType("application/json")
                .body(car)
                .when().post("/cars?price=25000&contacts=Bob 0969876543")
                .then()
                .body("id", greaterThan(0))
                .statusCode(200);
    }

    @Test
    public void shouldGetCarDetails() throws Exception {
        Car car = new Car("AB123", "BMW", 2007, "blue");
        long id = addCarAndGetId(car);
        given()
                .when().get("/cars/{id}", id)
                .then()
                .body("contacts", equalTo("Bob 0969876543"))
                .body("price", equalTo(25000))
                .statusCode(200);
    }

    @Test
    public void shouldGetCars() throws Exception {
        Car car = new Car("AC123", "BMW", 2007, "blue");
        long id = addCarAndGetId(car);
        JsonPath jsonResponse = given()
                .when().get("/cars").jsonPath();
        assert (jsonResponse.get("find { it.carId == " + id + " }.car.number").equals(car.getNumber()));
        assert (jsonResponse.get("find { it.carId == " + id + " }.car.brand").equals(car.getBrand()));
        assert (jsonResponse.get("find { it.carId == " + id + " }.car.year").equals(car.getYear()));
        assert (jsonResponse.get("find { it.carId == " + id + " }.car.color").equals(car.getColor()));
        assert (jsonResponse.get("find { it.carId == " + id + " }.saleInfo.contacts").equals("Bob 0969876543"));
        assert (jsonResponse.get("find { it.carId == " + id + " }.saleInfo.price").equals(25000));
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        Car car = new Car("AG123", "BMW", 2007, "blue");
        long id = addCarAndGetId(car);
        given().when().delete("/cars/{id}", id).then().statusCode(200);
        given().when().get("/cars").then().assertThat().body("id", not(hasValue(id)));
    }

    @Test
    public void shouldCreateDeal() throws Exception {
        Car car = new Car("AE123", "BMW", 2007, "blue");
        Customer customer = new Customer("Den", "0896543456");
        long id = addCarAndGetId(car);
        given()
                .contentType("application/json")
                .body(customer)
                .when().post("/deal?price=25000&carId={id}", id)
                .then()
                .body("id", greaterThan(0))
                .statusCode(200);
    }

    @Test
    public void shouldGetBestDeal() throws Exception {
        Car car = new Car("AR123", "BMW", 2007, "blue");
        Customer customer = new Customer("Den", "0896543456");
        long id = createDealAndGetOfferId(car, customer);
        given()
                .when().get("/offer/{id}", id)
                .then()
                .body("id", greaterThan(0))
                .statusCode(200);
    }

    @Test
    public void shouldAcceptDeal() throws Exception {
        Car car = new Car("AT123", "BMW", 2007, "blue");
        Customer customer = new Customer("Den", "0896543456");
        long id = createDealAndGetId(car, customer);
        given()
                .when().put("/acceptDeal/{id}", id)
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldRejectDeal() throws Exception {
        Car car = new Car("AU123", "BMW", 2007, "blue");
        Customer customer = new Customer("Den", "0896543456");
        long id = createDealAndGetId(car, customer);
        given()
                .when().put("/rejectDeal/{id}", id)
                .then()
                .statusCode(200);
    }

    private long addCarAndGetId(Car car) {
        return given()
                .contentType("application/json")
                .body(car)
                .when().post("/cars?price=25000&contacts=Bob 0969876543")
                .andReturn().getBody()
                .as(CarId.class).getId();
    }

    private long createDealAndGetId(Car car, Customer customer) {
        long id = addCarAndGetId(car);
        return given()
                .contentType("application/json")
                .body(customer)
                .when().post("/deal?price=25000&carId={id}", id)
                .andReturn().getBody()
                .as(DealInfo.class).getId();
    }

    private long createDealAndGetOfferId(Car car, Customer customer) {
        long id = addCarAndGetId(car);
        return given()
                .contentType("application/json")
                .body(customer)
                .when().post("/deal?price=25000&carId={id}", id)
                .andReturn().getBody()
                .as(DealInfo.class).getOfferId();
    }
}
