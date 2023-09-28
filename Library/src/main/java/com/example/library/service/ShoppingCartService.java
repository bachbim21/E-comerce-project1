package com.example.library.service;

import com.example.library.model.CartItem;
import com.example.library.model.Customer;
import com.example.library.model.Product;
import com.example.library.model.ShoppingCart;
import com.example.library.repository.CartItemRepo;
import com.example.library.repository.ShoppingCartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartService {

    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    ShoppingCartRepo shoppingCartRepo;
    public ShoppingCart addToCart(Product product, int quantity, Customer customer){
        ShoppingCart shoppingCart = customer.getShoppingCart();
        if (shoppingCart == null){
            shoppingCart = new ShoppingCart();
        }
        Set<CartItem> cartItems = shoppingCart.getCartItem();
        CartItem cartItem = findCartItem(cartItems,product.getProid());
        if (cartItems == null){
            cartItems = new HashSet<>();
            if (cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setUnit_price(quantity * product.getCost_price());
                cartItem.setQuantity(quantity);
                cartItem.setCart(shoppingCart);
                cartItems.add(cartItem);
                cartItemRepo.save(cartItem);
            }
        }
        else {
            if (cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setUnit_price(quantity * product.getCost_price());
                cartItem.setQuantity(quantity);
                cartItem.setCart(shoppingCart);
                cartItems.add(cartItem);
                cartItemRepo.save(cartItem);
            }
            else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.setUnit_price(cartItem.getUnit_price() + (quantity * product.getCost_price()));
                cartItemRepo.save(cartItem);
            }
        }
        shoppingCart.setCartItem(cartItems);
        int totalItem = totalItem(shoppingCart.getCartItem());
        double totalPrice = totalPrice(shoppingCart.getCartItem());
        shoppingCart.setTotal_items(totalItem);
        shoppingCart.setTotal_price(totalPrice);
        shoppingCart.setCustomer(customer);
        return shoppingCartRepo.save(shoppingCart);
    }

    public CartItem findCartItem(Set<CartItem> cartItems, Long productId){
        if (cartItems == null){
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems){
            if (item.getProduct().getProid() == productId){
                cartItem = item;
            }
        }
        return cartItem;
    }

    private int totalItem(Set<CartItem> cartItems){
        int t = 0;
        for (CartItem item : cartItems){
            t += item.getQuantity();
        }
        return t;
    }

    private double totalPrice(Set<CartItem> cartItems){
        int t = 0;
        for (CartItem item : cartItems){
            t += item.getUnit_price();
        }
        return t;
    }

     public ShoppingCart updateCart(Product product, int quantity, Customer customer){
        ShoppingCart shoppingCart = customer.getShoppingCart();
        Set<CartItem> cartItems = shoppingCart.getCartItem();
        CartItem item = findCartItem(cartItems, product.getProid());
        item.setQuantity(quantity);
        item.setUnit_price(quantity * product.getCost_price());
        cartItemRepo.save(item);

        int totalItem = totalItem(cartItems);
        double totalPrice = totalPrice(cartItems);
        shoppingCart.setTotal_price(totalPrice);
        shoppingCart.setTotal_items(totalItem);
        return shoppingCartRepo.save(shoppingCart);

    }

    public ShoppingCart deleteCart(Product product, Customer customer){
        ShoppingCart shoppingCart = customer.getShoppingCart();
        Set<CartItem> cartItems = shoppingCart.getCartItem();
        CartItem item = findCartItem(cartItems, product.getProid());
        cartItems.remove(item);
        cartItemRepo.delete(item);

        int totalItem = totalItem(cartItems);
        double totalPrice = totalPrice(cartItems);
        shoppingCart.setTotal_price(totalPrice);
        shoppingCart.setTotal_items(totalItem);
        return shoppingCartRepo.save(shoppingCart);
    }

    public void deleteCartById(Long id){
        ShoppingCart shoppingCart = shoppingCartRepo.getById(id);
       for (CartItem item : shoppingCart.getCartItem()){
           cartItemRepo.deleteById(item.getId());
       }
        shoppingCart.setCustomer(null);
        shoppingCart.getCartItem().clear();
        shoppingCart.setTotal_price(0);
        shoppingCart.setTotal_items(0);

        shoppingCartRepo.save(shoppingCart);
    }

}
