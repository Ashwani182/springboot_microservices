package com.springmsservices.inventory.inventoryservice.service;

import com.springmsservices.inventory.inventoryservice.dto.InventoryResponse;
import com.springmsservices.inventory.inventoryservice.model.Inventory;
import com.springmsservices.inventory.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j //for logs
public class InventoryService {
    private final InventoryRepository inventoryRepository;


    //old code to check single inventory

/*    @Transactional(readOnly = true)// It is used to manage the transaction automatically by springboot
    public Boolean isInventoryExist(String skuCode){
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }*/

    // to get list of inventory
    @Transactional(readOnly = true)// It is used to manage the transaction automatically by springboot
    @SneakyThrows // to handle the Exception because of sleep thread
    public List<InventoryResponse> isInventoryExist(List<String> skuCode){
        log.info("Wait Started");
        Thread.sleep(10000);
        log.info("wait Ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream().map(this::toDto).toList();
    }

    private InventoryResponse toDto(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInstock(inventory.getQuantity()>0)
                .build();

    }


}
