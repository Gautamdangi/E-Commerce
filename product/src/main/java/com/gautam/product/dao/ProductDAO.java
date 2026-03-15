package com.gautam.product.dao;

import com.gautam.product.model.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository <ProductDetails, Long> {//"write according to requirement"



    List<ProductDetails> findByIsActiveTrue();//RETURNS COMPLETE LIST OF ITEM
    Page<ProductDetails> findAllByIsActiveTrue(Pageable pageable);//RETURNS ACCORDING TO THE PAGINATION
}
