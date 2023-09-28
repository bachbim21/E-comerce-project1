package com.example.library.repository;

import com.example.library.dto.CategoryDto;
import com.example.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CateRepo extends JpaRepository<Category,Long> {
    @Query("select c from Category c where c.is_activated = true and c.is_deleted = false ")
    List<Category> findAllByActivated();

    //customer

    @Query("select new com.example.library.dto.CategoryDto(c.cateid, c.name, count(p.category.cateid)) " +
            "from Category c inner join Product p on p.category.cateid = c.cateid where c.is_activated = true " +
            "and c.is_deleted = false group by c.cateid")
    List<CategoryDto> listMenu();
}
