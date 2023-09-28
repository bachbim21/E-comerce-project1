package com.example.library.service;

import com.example.library.model.Country;
import com.example.library.repository.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    @Autowired
    CountryRepo countryRepo;

    public List<Country> countryList(){
        return countryRepo.findAll();
    }
}
