package com.example.library.service;

import com.example.library.dto.ProductDto;
import com.example.library.model.Product;
import com.example.library.repository.ProductRepo;
import com.example.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ImageUpload imageUpload;

    public List<ProductDto> findAll(){
        List<ProductDto> productDtoList = new ArrayList<>();
        List<Product> productList = productRepo.findAll();
        for (Product product : productList){
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getProid());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCostPrice(product.getCost_price());
            productDto.setSalePrice(product.getSale_price());
            productDto.setCurrentQuantity(product.getCurrent_quantity());
            productDto.setCategory(product.getCategory());
            productDto.setImage(product.getImage());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDtoList.add(productDto);
        }
        return productDtoList ;
    }

    public ProductDto getById(Long id){
        Product product = productRepo.getById(id);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getProid());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCost_price());
        productDto.setSalePrice(product.getSale_price());
        productDto.setCurrentQuantity(product.getCurrent_quantity());
        productDto.setCategory(product.getCategory());
        productDto.setImage(product.getImage());
        productDto.setActivated(product.is_activated());
        productDto.setDeleted(product.is_deleted());
        return productDto;
    }

    public Product save(ProductDto productDto, MultipartFile imageProduct){

        try {
            Product product = new Product();
            if (imageProduct == null){
                product.setImage(null);
            }else {
                imageUpload.uploadImage(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCost_price(productDto.getCostPrice());
            product.setCurrent_quantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            return productRepo.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Product update(MultipartFile imageProduct, ProductDto productDto){
        try{
            Product product = productRepo.getById(productDto.getId());
            if (imageProduct == null){
                product.setImage(product.getImage());
            }else {
                if (imageUpload.checkExisted(imageProduct) == false){
                    imageUpload.uploadImage(imageProduct);
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCost_price(productDto.getCostPrice());
            product.setSale_price(productDto.getSalePrice());
            product.setCurrent_quantity(productDto.getCurrentQuantity());
            return productRepo.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void deleteById(Long id){
        Product product = productRepo.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepo.save(product);
    }

    public void enableById(Long id){
        Product product = productRepo.getById(id);
        product.set_deleted(false);
        product.set_activated(true);
        productRepo.save(product);
    }

    public Page<Product> pageProduct(int pageNo){
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Product> productPage = productRepo.pageProduct(pageable);
        return productPage;
    }

    public Page<Product> search (String keyword, int pageNo){
        Pageable pageable = PageRequest.of(pageNo,5);
        Page<Product> product = productRepo.searchProductt(keyword, pageable);
        return product;
    }

    //customer
    public List<Product> getAllProduct(){
        return productRepo.getAllProduct();
    }

    public List<Product> listViewProduct(){
        return productRepo.listViewProduct();
    }

    public Product getProductByID(Long id){
        return productRepo.getById(id);
    }

    public List<Product> getRelated(Long cateid){
        return productRepo.getRelated(cateid);
    }

    public List<Product> getProductInListCate(Long id){
        return productRepo.getProductInListCate(id);
    }

    public List<Product> sortASC(){
        return productRepo.sortASC();
    }

    public List<Product> sortDESC(){
        return productRepo.sortDESC();
    }

}
