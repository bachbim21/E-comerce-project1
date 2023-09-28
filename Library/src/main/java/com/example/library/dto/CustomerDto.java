package com.example.library.dto;

import com.example.library.model.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private String repeatPassword;
    private String phoneNumber;
    private String address;
    private City city;
    private String image;
    private String country;


}
