package com.springmsservices.inventory.inventoryservice.repository;

import com.springmsservices.inventory.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Optional<Inventory> findBySkuCode(String skuCode);
    // JPA repository automatically creates the query
    // provided passed param should be exact same name as in entity

    List<Inventory> findBySkuCodeIn(List<String> skuCode); // to create query for search with list of skuCode

}
