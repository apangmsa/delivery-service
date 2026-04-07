package org.iimsa.deliveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DeliveryserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryserverApplication.class, args);
	}

}
