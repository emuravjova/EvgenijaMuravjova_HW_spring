package com.playtika.automation.carshop.web;

import com.playtika.automation.carshop.domain.Car;
import com.playtika.automation.carshop.domain.CarSaleDetails;
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
        long id = given()
                .contentType("application/json")
                .body(car)
                .when().post("/cars?price=25000&contacts=Bob 0969876543")
                .andReturn().getBody()
                .as(CarSaleDetails.class).getId();

        given()
                .when().get("/cars/{id}",id)
                .then()
                .body("contacts",equalTo("Bob 0969876543"))
                .body("price",equalTo(25000))
                .statusCode(200);
    }

    @Test
    public void shouldGetCars() throws Exception {
        Car car = new Car("BMW","2010");
        long id = given()
                .contentType("application/json")
                .body(car)
                .when().post("/cars?price=25000&contacts=Bob 0969876543")
                .andReturn().getBody()
                .as(CarSaleDetails.class).getId();

        JsonPath jsonResponse = given()
                .when().get("/cars").jsonPath();

                assert(jsonResponse.get("find { it.id == "+ id +" }.car.name").equals(car.getName()));
                assert(jsonResponse.get("find { it.id == "+ id +" }.car.model").equals(car.getModel()));
                assert(jsonResponse.get("find { it.id == "+ id +" }.contacts").equals("Bob 0969876543"));
                assert(jsonResponse.get("find { it.id == "+ id +" }.price").equals(25000));
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        Car car = new Car("BMW","2010");
        long id = given()
                .contentType("application/json")
                .body(car)
                .when().post("/cars?price=25000&contacts=Bob 0969876543")
                .andReturn().getBody()
                .as(CarSaleDetails.class).getId();

        given().when().delete("/cars/{id}",id).then().statusCode(200);
        given().when().get("/cars").then().assertThat().body("id",not(hasValue(id)));
    }
}
