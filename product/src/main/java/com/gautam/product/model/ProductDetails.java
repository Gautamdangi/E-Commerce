package com.gautam.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name ="brand")
    private String brand;

    @Column(name="description",length = 1000)
    private String description;


    @Column(name = "price",nullable = false)
   // @DecimalMin(value = "0.01",inclusive = false)
    @Positive(message = "Only positive values are allowed") //now only positive values will be stored in db
    private BigDecimal price; //Avoids floating-point precision issues (e.g., 10.10 != 10.1 + 0.00);

    @Column(name="category",nullable = false)
    private String category;

    @Column(name = "is_active",nullable = false)//find all only where isActive is true -> productdao methods
    private Boolean isActive;//for soft delete not completely remove

    @Column(name = "quantity")
    private Integer quantity;



    //to prevent duplicity of product of SKU
//    A SKU (Stock Keeping Unit) is a unique alphanumeric code assigned by a retailer to identify and track a
//    specific product and its variations within inventory systems.
// SKU is made of combination of unique char in product details





    //constructor for testing
    public ProductDetails(){
    }

}
