package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadOrderDTO {
    String customerBadFirstName;
    String customerLastName;
    String billingStreet;
    String billingCity;
}