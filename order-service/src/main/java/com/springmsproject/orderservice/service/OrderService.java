package com.springmsproject.orderservice.service;

import com.springmsproject.orderservice.dto.InventoryResponse;
import com.springmsproject.orderservice.dto.OrderLineItemsDto;
import com.springmsproject.orderservice.dto.OrderRequest;
import com.springmsproject.orderservice.event.OrderPlacedEvent;
import com.springmsproject.orderservice.model.Order;
import com.springmsproject.orderservice.model.OrderLineItems;
import com.springmsproject.orderservice.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;



import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer; // TO create span id for particular call
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    //kafka template is used for communication between the services. it uses the Key value Pair to send the notification

    public String createOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
       List<OrderLineItems> orderLineItemsList= orderRequest.getOrderLineItemListDto().stream()
                                                            .map(this::getOrderLineItems).toList();
        order.setOrderLineItemList(orderLineItemsList);

       //Old code to check the individual but we need to change the
        //inventory service to check all the products preset in the orderitem list
     // Boolean isintheList= webClient.get().uri("http://localhost:8082/api/inventory").retrieve().bodyToMono(Boolean.class).block();

        //to check list of element
        List<String> skuCodes=orderLineItemsList.stream().map(OrderLineItems::getSkuCode).toList();

        //this will help to create span id for this particular service call whenever the service is get called
        Span inventoryServiceLookup= tracer.nextSpan().name("InventoryServiceLookup");
        try(Tracer.SpanInScope spanInScope= tracer.withSpan(inventoryServiceLookup.start())){

            //call inventory service and place order
            InventoryResponse[] inventoryResponseArray= webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductIsInStock = false;
            if (inventoryResponseArray!= null && inventoryResponseArray.length!=0) {
                allProductIsInStock = Arrays.stream(inventoryResponseArray)
                        .allMatch(InventoryResponse::isInstock);
            }

            if(allProductIsInStock){
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
                //send notification to kafka, Kafak template as key and value pair which it sends
                // we are sending object so we need serilasers for both one for key and for value
                return "Order placed Successfully";
            }else {
                throw new IllegalArgumentException("Product is not available in the stock");
            }
        }finally {
            inventoryServiceLookup.end();
        }


    }

    public OrderLineItems getOrderLineItems(OrderLineItemsDto orderLineItemsDto){
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .skuCode(orderLineItemsDto.getSkuCode())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}
