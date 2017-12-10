package com.playtika.automation.carshop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication(exclude={WebSocketAutoConfiguration.class})
public class CarshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarshopApplication.class, args);
	}

}
