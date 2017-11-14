package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CarControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    @Test
    public void shouldAddCar() throws Exception {
        Car car = new Car("BMW","2010");
                given()
                .contentType("application/json")
                .body(car)
                .when().post("/cars?price=25000&contacts=Bob 0969876543")
                .then()
                    .body("id",greaterThan(0))
                    .body("car.name",equalTo("BMW"))
                    .body("car.model",equalTo("2010"))
                    .body("contacts",equalTo("Bob 0969876543"))
                    .body("price",equalTo(25000))
                    .statusCode(200);
    }

    @Test
    public void shouldGetCarDetails() throws Exception {
        Car car = new Car("BMW","2010");
        int id = given()
                .contentType("application/json")
                .body(car)
                .when().post("/cars?price=25000&contacts=Bob 0969876543")
                .andReturn().getBody().jsonPath().get("id");
        given()
                .when().get("/cars/{id}",id).then()
                .body("contacts",equalTo("Bob 0969876543"))
                .body("price",equalTo(25000))
                .statusCode(200);
    }
}
