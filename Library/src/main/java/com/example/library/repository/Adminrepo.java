package com.example.library.repository;

import com.example.library.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Adminrepo extends JpaRepository<Admin, Long> {
    public Admin findByUsername(String username);
}
