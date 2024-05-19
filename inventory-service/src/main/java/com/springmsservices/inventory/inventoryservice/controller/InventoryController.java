package com.springmsservices.inventory.inventoryservice.controller;

import com.springmsservices.inventory.inventoryservice.dto.InventoryResponse;
import com.springmsservices.inventory.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    //with path variable our request URI = http://localhost:8082/api/inventory/iphone-13,iphone-14,..
    // with RequestParam URI = htp://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone-14...
    //We want to follow the second one
    //old code with path variable

//    @GetMapping("/{sku-code}") // PathVariable to paas in URI
//    @ResponseStatus(HttpStatus.OK)
//    public Boolean isInStock(@PathVariable("sku-code") String skuCode){ // need to define here in method als as path variable
//        return inventoryService.isInventoryExist(skuCode);
//    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){ // need to define here in method als as path variable
        return inventoryService.isInventoryExist(skuCode);
    }
}
