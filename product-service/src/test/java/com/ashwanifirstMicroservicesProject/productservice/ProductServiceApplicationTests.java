package com.ashwanifirstMicroservicesProject.productservice;

import com.ashwanifirstMicroservicesProject.productservice.dto.ProductRequest;
import com.ashwanifirstMicroservicesProject.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc //to Auto configure the Mock MVC
class ProductServiceApplicationTests {
    @Container // to show container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	//making static to use as methods directly as a reference
	//it will gve a docker container for MongoDB need to provide docker image version basically mongodb version
	// at start it will download that docker image

	@Autowired
	private MockMvc mockMvc;  //Mock MVC gives the all the environments to run the like servlet we can hit post,get call

	@Autowired
	private ObjectMapper objectMapper; //Using to map object to string
	@Autowired
	    private ProductRepository productRepository;
	static {
	        mongoDBContainer.start();
	    }
		
   @DynamicPropertySource  //indicates dynamic properties source . it will set properties dynamically as start of test
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
		// DynamicPropertyRegistry  provide register to add the properties
	}
	
	@Test
	void testCreateProduct() throws Exception {
		ProductRequest productRequest=getProductRequest();
		  mockMvc.perform(MockMvcRequestBuilders.post("/v1/product")
				  .contentType(MediaType.APPLICATION_JSON)
				  .content(objectMapper.writeValueAsString(productRequest)))
				  .andExpect(status().isCreated());
		Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
	   return ProductRequest.builder()
			   .name("Ipad")
			   .description("Ipad Air")
			   .price(BigDecimal.valueOf(40000)) // to convert to big-decimal
			   .build();
	}

}
