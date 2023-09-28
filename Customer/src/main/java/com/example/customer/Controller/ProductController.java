package com.example.customer.Controller;

import com.example.library.dto.CategoryDto;
import com.example.library.model.Category;
import com.example.library.model.Product;
import com.example.library.service.CateService;
import com.example.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CateService cateService;

    @GetMapping("/product")
    public String product(Model model){
        model.addAttribute("title", "shop");
        List<CategoryDto> categoryDtoListView = cateService.listMenu();
        List<Product> productList = productService.getAllProduct();
        List<Product> listview = productService.listViewProduct();
        model.addAttribute("product", productList);
        model.addAttribute("list", listview);
        model.addAttribute("cate", categoryDtoListView);
        return "shop";
    }

    @GetMapping("/findProduct/{id}")
    public String findProduct (@PathVariable("id") Long id, Model model){
        model.addAttribute("title", "product-detail");
        Product product = productService.getProductByID(id);
        Long cateid = product.getCategory().getCateid();
        List<Product> productListRelated = productService.getRelated(cateid);
        model.addAttribute("product", product);
        model.addAttribute("related", productListRelated);
        return "product-detail";
    }

    @GetMapping("/productInCategory/{id}")
    public String productInCategory(@PathVariable("id") Long id ,Model model){
        model.addAttribute("title", "products-in-category");
        Category category = cateService.findById(id);
        List<CategoryDto> categoryDtoList = cateService.listMenu();
        List<Product> productList = productService.getProductInListCate(id);
        model.addAttribute("category", category);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("product", productList);
        return "products-in-category";
    }

    @GetMapping("/ascPrice")
    public String sortASC(Model model){
        List<Category> categoryList = cateService.findAllByActivated();
        List<Product> productList = productService.sortASC();
        List<CategoryDto> categoryDtoList = cateService.listMenu();
        model.addAttribute("category", categoryList);
        model.addAttribute("product", productList);
        model.addAttribute("categoryDtoList", categoryDtoList);
        return "filter-low-price";
    }

    @GetMapping("/descPrice")
    public String descPrice(Model model){
        List<Category> categoryList = cateService.findAllByActivated();
        List<Product> productList = productService.sortDESC();
        List<CategoryDto> categoryDtoList = cateService.listMenu();
        model.addAttribute("category", categoryList);
        model.addAttribute("product", productList);
        model.addAttribute("categoryDtoList", categoryDtoList);
        return "filter-high-price";
    }
}
