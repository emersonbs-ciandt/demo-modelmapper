package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    Customer customer;
    Address billing;
    String optionalName;
}

