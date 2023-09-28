package com.example.library.repository;

import com.example.library.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface CountryRepo extends JpaRepository<Country, Long> {
}
