package com.example.customer.Controller;

import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.model.Customer;
import com.example.library.model.ShoppingCart;
import com.example.library.service.CateService;
import com.example.library.service.CustomerService;
import com.example.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ProductService productService;

    @Autowired
    CateService cateService;

    @Autowired
    CustomerService customerService;

    @GetMapping("/menu")
    public String index(Model model){
        List<Category> categoryList = cateService.findAll();
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("title", "index");
        model.addAttribute("category", categoryList);
        model.addAttribute("product", productDtoList);
        return "index";
    }

    @GetMapping("/")
    public String home(Model model, Principal principal, HttpSession session){
        if (principal != null){
            session.setAttribute("username", principal.getName());
            Customer customer = customerService.findByUserName(principal.getName());
            ShoppingCart shoppingCart = customer.getShoppingCart();
            session.setAttribute("totalItem", shoppingCart.getTotal_items());
        }else {
            session.removeAttribute("username");
        }
        model.addAttribute("title", "home");
        return "home";
    }

}
