package com.example.customer.Controller;

import com.example.library.dto.CustomerDto;
import com.example.library.model.City;
import com.example.library.model.Country;
import com.example.library.model.Customer;
import com.example.library.service.CityService;
import com.example.library.service.CountryService;
import com.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    CustomerService customerService;

    @Autowired
    CityService cityService;

    @Autowired
    CountryService countryService;

    @GetMapping("/myAccount")
    public String accountHome(Model model, Principal principal){
        model.addAttribute("title", "account");
        if (principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        List<Country> countryList = countryService.countryList();
        List<City> cityList = cityService.cityList();

        model.addAttribute("customer", customer);
        model.addAttribute("city", cityList);
        model.addAttribute("country", countryList);

        return "account";
    }

    @PostMapping("/update-infor")
    public String updateInformation(@ModelAttribute("customer") Customer customer, RedirectAttributes attributes, Model model, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }

        List<Country> countryList = countryService.countryList();
        List<City> cityList = cityService.cityList();
        model.addAttribute("city", cityList);
        model.addAttribute("country", countryList);

        customerService.update(customer);

        Customer customer1 = customerService.findByUserName(principal.getName());
        attributes.addFlashAttribute("success", "Update successfully!");
        model.addAttribute("customer", customer1);
        return "redirect:/myAccount";
    }
}
