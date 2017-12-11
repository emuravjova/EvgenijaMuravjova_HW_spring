package com.playtika.automation.carshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"com.playtika.automation.carshop.dao.jpa", "com.playtika.automation.carshop.dao.entity"})
@EnableCouchbaseRepositories({"com.playtika.automation.carshop.dao.couchbase", "com.playtika.automation.carshop.dao.entity"})
@SpringBootApplication(exclude={WebSocketAutoConfiguration.class})
public class CarshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarshopApplication.class, args);
	}

}
