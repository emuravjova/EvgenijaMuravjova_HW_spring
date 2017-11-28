package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.web.dto.CarId;
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
        Car car = new Car("AA123","BMW", 2007,"blue");
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
        Car car = new Car("AB123","BMW", 2007,"blue");
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
        Car car = new Car("AC123","BMW", 2007,"blue");
        long id = addCarAndGetId(car);
        JsonPath jsonResponse = given()
                .when().get("/cars").jsonPath();
        assert(jsonResponse.get("find { it.id == "+ id +" }.car.number").equals(car.getNumber()));
        assert(jsonResponse.get("find { it.id == "+ id +" }.car.brand").equals(car.getBrand()));
        assert(jsonResponse.get("find { it.id == "+ id +" }.car.year").equals(car.getYear()));
        assert(jsonResponse.get("find { it.id == "+ id +" }.car.color").equals(car.getColor()));
        assert(jsonResponse.get("find { it.id == "+ id +" }.saleInfo.contacts").equals("Bob 0969876543"));
        assert(jsonResponse.get("find { it.id == "+ id +" }.saleInfo.price").equals(25000));
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        Car car = new Car("AD123","BMW", 2007,"blue");
        long id = addCarAndGetId(car);
        given().when().delete("/cars/{id}",id).then().statusCode(200);
        given().when().get("/cars").then().assertThat().body("id",not(hasValue(id)));
    }

    private long addCarAndGetId(Car car) {
        return given()
                .contentType("application/json")
                .body(car)
                .when().post("/cars?price=25000&contacts=Bob 0969876543")
                .andReturn().getBody()
                .as(CarId.class).getId();
    }
}
