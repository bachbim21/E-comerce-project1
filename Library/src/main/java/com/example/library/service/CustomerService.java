package com.example.library.service;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;
import com.example.library.repository.CustomerRepo;
import com.example.library.repository.Rolerepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomerService {
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    Rolerepo rolerepo;

    @Autowired
    CustomerRepo customerRepo;

    public Customer findByUserName(String username){
        return customerRepo.findByUsername(username);
    }

    public CustomerDto save(CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setFirst_name(customerDto.getFirst_name());
        customer.setLast_name(customerDto.getLast_name());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setRoles(Arrays.asList(rolerepo.findByName("CUSTOMER")));
        Customer customerSave = customerRepo.save(customer);
        return mapperDto(customerSave);
    }

    public CustomerDto mapperDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirst_name(customer.getFirst_name());
        customerDto.setLast_name(customer.getLast_name());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        return customerDto;
    }

    public Customer update(Customer customer){
        Customer customer1 = customerRepo.findByUsername(customer.getUsername());
        customer1.setAddress(customer.getAddress());
        customer1.setCity(customer.getCity());
        customer1.setCountry(customer.getCountry());
        customer1.setPhone_number(customer.getPhone_number());
        return customerRepo.save(customer1);
    }

}
