package com.example.admin.controller;

import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.model.Product;
import com.example.library.service.CateService;
import com.example.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CateService cateService;

    @GetMapping("/product")
    public String product(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("product", productDtoList);
        model.addAttribute("size", productDtoList.size());
        model.addAttribute("title", "Product");
        return "product";
    }

    @GetMapping("/addProduct")
    public String addProduct(Model model){
        List<Category> categoryList = cateService.findAllByActivated();
        model.addAttribute("category", categoryList);
        model.addAttribute("title", "Add Product");
        return "addProduct";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(Model model, @ModelAttribute("product") ProductDto productDto,
                              @RequestParam("imageProduct")MultipartFile image,
                              RedirectAttributes attributes){

        try{
            productService.save(productDto, image);
            attributes.addFlashAttribute("success","Add successfully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/addProduct";
    }

    @GetMapping("/updateProduct/{proid}")
    public String updateProduct(@PathVariable("proid") Long proid, Model model,Principal principal){
       if (principal == null){
           return "redirect:/login";
       }
        model.addAttribute("title", "UpdateProduct");
        List<Category> categoryList = cateService.findAllByActivated();
        ProductDto productDto = productService.getById(proid);
        model.addAttribute("category", categoryList);
        model.addAttribute("product", productDto);
        return "updateProduct";
    }

    @PostMapping("/updateProduct/{id}")
    public String updateProductAction(@PathVariable("id") Long id, @RequestParam("imageProduct") MultipartFile imageProduct
            ,@ModelAttribute("product") ProductDto productDto, RedirectAttributes attributes){
        try {
            productService.update(imageProduct, productDto);
            attributes.addFlashAttribute("success", "Update success");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/product/0";
    }

    @GetMapping("/enableProduct/{id}")
    public String enableProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success", " success");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/product/0";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", " success");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/product/0";
    }

    @GetMapping("/product/{pageNo}")
    public String page(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<Product> productPage = productService.pageProduct(pageNo);
        model.addAttribute("title", "Product");
        model.addAttribute("totalPage", productPage.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("product", productPage);
        return "product";
    }

    @GetMapping("/search/{pageNo}")
    public String search(@PathVariable("pageNo") int pageNo, @RequestParam("keyword") String keyword ,Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<Product> productPage = productService.search(keyword,pageNo);
        model.addAttribute("title", "Search");
        model.addAttribute("totalPage", productPage.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("product", productPage);
        return "product";
    }
}
