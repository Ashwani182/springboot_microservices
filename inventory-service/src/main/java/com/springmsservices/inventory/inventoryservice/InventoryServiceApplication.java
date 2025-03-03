package com.springmsservices.inventory.inventoryservice;

import com.springmsservices.inventory.inventoryservice.model.Inventory;
import com.springmsservices.inventory.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
   // commandline runner it will run first when program starts
	// It returns functional interface which runs the code block inside that
    @Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iphone_15");
			inventory1.setQuantity(50);

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);

		};
	}
}
