package com.example.library.repository;

import com.example.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    @Query("select p from Product p")
    Page<Product> pageProduct(Pageable pageable);

    @Query("select p from Product p where p.name like %?1%")
    Page<Product> searchProductt(String keyword, Pageable pageable);

    //customer

    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false ")
    List<Product> getAllProduct();

    @Query(value = "select * from products p where p.is_activated = true order by rand() asc ", nativeQuery = true)
    List<Product> listViewProduct();

    @Query(value = "select * from products p inner join categories c on c.category_id = p.category_id where p.category_id = ?1", nativeQuery = true)
    List<Product> getRelated(Long cateid);

    @Query("select p from Product p inner join Category c on c.cateid = p.category.cateid where c.cateid = ?1 and p.is_activated = true and p.is_deleted = false ")
    List<Product> getProductInListCate(Long id);

    @Query("select p from Product p where p.is_deleted = false and p.is_activated = true order by p.cost_price asc ")
    List<Product> sortASC();

    @Query("select p from Product p where p.is_deleted = false and p.is_activated = true order by p.cost_price desc ")
    List<Product> sortDESC();
}
