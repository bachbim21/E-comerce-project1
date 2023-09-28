package com.example.library.service;

import com.example.library.dto.AdminDto;
import com.example.library.model.Admin;
import com.example.library.repository.Adminrepo;
import com.example.library.repository.Rolerepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminService {

    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    Adminrepo adminrepo;

    @Autowired
    Rolerepo rolerepo;

    public Admin findByUserName(String username){
        return adminrepo.findByUsername(username);
    }

    public Admin save(AdminDto adminDto){
        Admin admin = new Admin();
        admin.setFirstname(adminDto.getFirstname());
        admin.setLastname(adminDto.getLastname());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setRoles(Arrays.asList(rolerepo.findByName("ADMIN")));
        return adminrepo.save(admin);
    }
}
