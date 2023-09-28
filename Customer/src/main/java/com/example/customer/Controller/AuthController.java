package com.example.customer.Controller;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;
import com.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("customerDto", new CustomerDto());
        return "register";
    }

    @PostMapping("/do-register")
    public String registerAction(@ModelAttribute("customerDto") CustomerDto customerDto, BindingResult result, Model model, RedirectAttributes attributes){

        try{
            if (result.hasErrors()){
                model.addAttribute("customerDto", customerDto);
                return "register";
            }

            Customer customer = customerService.findByUserName(customerDto.getUsername());
            if (customer != null){
                model.addAttribute("usernameexisted", "Username existed");
                model.addAttribute("customerDto", customerDto);
            }else {
                if (customerDto.getPassword().equals(customerDto.getRepeatPassword())){
                    customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                    customerService.save(customerDto);
                    attributes.addFlashAttribute("success", "Register OK");
                }
                else {
                    model.addAttribute("password", "Password is not the same");
                    model.addAttribute("customerDto",customerDto);
                }
            }
            return "register";

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error", "Error");
            model.addAttribute("customerDto",customerDto);
        }
        return "register";
    }

}
