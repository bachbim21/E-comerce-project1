package com.example.customer.Controller;

import com.example.library.model.Customer;
import com.example.library.model.Product;
import com.example.library.model.ShoppingCart;
import com.example.library.service.CustomerService;
import com.example.library.service.ProductService;
import com.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class CartController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    ProductService productService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session){
        model.addAttribute("title", "cart");
        if (principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        if (shoppingCart == null){
            model.addAttribute("check", "No item found");
        }
        double subTotal = shoppingCart.getTotal_price();
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("shoppingCart", shoppingCart);
        session.setAttribute("totalItem", shoppingCart.getTotal_items());
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long id, @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
        Principal principal, Model model, HttpServletRequest request){
        if (principal == null){
            return "redirect:/login";
        }
        Product product = productService.getProductByID(id);
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = shoppingCartService.addToCart(product, quantity,customer);

        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping ( value = "/updateCart", method = RequestMethod.POST ,params = "action=update")
    public String updateCart(@RequestParam("id") Long id, @RequestParam("quantity") int quantity, Principal principal, Model model){
        if (principal == null){
            return "redirect:/login";
        }
        Product product = productService.getProductByID(id);
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = shoppingCartService.updateCart(product,quantity,customer);
        model.addAttribute("shoppingCart", shoppingCart);
        return "redirect:/cart";
    }

    @RequestMapping (value = "/updateCart", method = RequestMethod.POST ,params = "action=delete")
    public String deleteCart(@RequestParam("id") Long id, Principal principal, Model model){
        if (principal == null){
            return "redirect:/login";
        }
        Product product = productService.getProductByID(id);
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = shoppingCartService.deleteCart(product,customer);
        model.addAttribute("shoppingCart", shoppingCart);
        return "redirect:/cart";
    }



}
