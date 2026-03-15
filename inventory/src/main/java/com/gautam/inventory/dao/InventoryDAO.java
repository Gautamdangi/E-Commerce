package com.gautam.inventory.dao;

import com.gautam.inventory.model.InventoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryDAO extends JpaRepository<InventoryDetails, Long>{
    Optional<InventoryDetails> findByProductId(Long productId);
}
