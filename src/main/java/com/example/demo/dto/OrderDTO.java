package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    String customerFirstName;
    String customerLastName;
    String billingStreet;
    String billingCity;
}