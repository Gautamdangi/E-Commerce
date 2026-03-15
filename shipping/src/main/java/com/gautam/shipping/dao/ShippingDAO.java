package com.gautam.shipping.dao;

import com.gautam.shipping.model.ShippingEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ShippingDAO  extends JpaRepository<ShippingEntity,Long> {


    Optional<ShippingEntity> findByOrderId(Long aLong);


}
