package com.example.customer.Controller;

import com.example.library.model.*;
import com.example.library.service.CityService;
import com.example.library.service.CountryService;
import com.example.library.service.CustomerService;
import com.example.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    CustomerService customerService;

    @Autowired
    CountryService countryService;
    @Autowired
    CityService cityService;

    @Autowired
    OrderService orderService;

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);

        if (customer.getCity() == null || customer.getPhone_number() == null || customer.getAddress() == null ){

            List<Country> countryList = countryService.countryList();
            List<City> cityList = cityService.cityList();
            model.addAttribute("city", cityList);
            model.addAttribute("country", countryList);

            model.addAttribute("customer", customer);
            model.addAttribute("error","Not enough information");
            return "account";
        }else {
            ShoppingCart shoppingCart = customer.getShoppingCart();
            model.addAttribute("customer", customer);
            model.addAttribute("shoppingCart", shoppingCart);
            model.addAttribute("grandTotal", shoppingCart.getTotal_price());

            List<Country> countryList = countryService.countryList();
            List<City> cityList = cityService.cityList();
            model.addAttribute("city", cityList);
            model.addAttribute("country", countryList);

            return "checkout";
        }
    }

    @GetMapping("/order")
    public String order(Model model, Principal principal){
        model.addAttribute("title", "order");
        if (principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        List<Order> orderList = customer.getOrderList();
        model.addAttribute("order", orderList);

        return "order";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        orderService.save(shoppingCart);
         return "redirect:/order";
    }
}
