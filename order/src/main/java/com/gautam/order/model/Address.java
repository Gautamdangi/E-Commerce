package com.gautam.order.model;


import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable//used to define object properties should be stored in same entity in db table not as separate entity table
public class Address {
    private String fullName;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String pincode;
}
// also prevents inconsistency if user updates profile