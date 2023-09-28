package com.example.library.service;

import com.example.library.dto.CategoryDto;
import com.example.library.model.Category;
import com.example.library.repository.CateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CateService {
    @Autowired
    CateRepo cateRepo;

    public List<Category> findAll(){

        return cateRepo.findAll();
    }

    public Category save(Category category){
        Category category1 = new Category(category.getName());
        return cateRepo.save(category1);
    }

    public Category findById(Long cateid){
        return cateRepo.findById(cateid).get();
    }

    public Category update(Category category){
        Category category1 = cateRepo.findById(category.getCateid()).get();
        category1.setName(category.getName());
        category1.set_activated(category.is_activated());
        category1.set_deleted(category.is_deleted());
        return cateRepo.save(category1);
    }

    public void deleteById(Long cateid){
        Category category = cateRepo.getById(cateid);
        category.set_deleted(true);
        category.set_activated(false);
        cateRepo.save(category);
    }

    public void enableById(Long cateid){
        Category category = cateRepo.getById(cateid);
        category.set_deleted(false);
        category.set_activated(true);
        cateRepo.save(category);
    }

    public List<Category> findAllByActivated(){
        return cateRepo.findAllByActivated();
    }

    //customer

    public List<CategoryDto> listMenu(){
        return cateRepo.listMenu();
    }


}
