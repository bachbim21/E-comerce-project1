package com.example.library.service;

import com.example.library.model.City;
import com.example.library.repository.CityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    CityRepo cityRepo;

    public List<City> cityList(){
        return cityRepo.findAll();
    }
}
