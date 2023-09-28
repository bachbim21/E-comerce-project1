package com.example.library.service;

import com.example.library.model.*;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    OrderDetailRepo detailRepo;

    @Autowired
    ShoppingCartRepo shoppingCartRepo;

    @Autowired
    CartItemRepo cartItemRepo;



    public void save(ShoppingCart shoppingCart){
        Order order = new Order();
        order.setOrder_date(new Date());
        order.setCustomer(shoppingCart.getCustomer());
        order.setTax(2);
        order.setTotal_price(shoppingCart.getTotal_price());
        order.set_accept(false);
        order.setPayment_method("Cash");
        order.setOrder_status("Pending");
        order.setQuantity(shoppingCart.getTotal_items());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItem()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            detailRepo.save(orderDetail);
            orderDetailList.add(orderDetail);
            cartItemRepo.delete(item);
        }
        order.setOrderDetailList(orderDetailList);
        shoppingCart.setCartItem(new HashSet<>());
        shoppingCart.setTotal_items(0);
        shoppingCart.setTotal_price(0);
        shoppingCartRepo.save(shoppingCart);
        orderRepo.save(order);
    }

    public List<Order> find(String username){
        Customer customer = customerRepo.findByUsername(username);
        List<Order> orderList = customer.getOrderList();
        return orderList;
    }
    public List<Order> findAll(){
        return orderRepo.findAll();
    }

    public Order acceptOrder(Long id) {
        Order order = orderRepo.getById(id);
        order.set_accept(true);
        order.setDelivery_date(new Date());
        return orderRepo.save(order);
    }

    public void cancelOrder(Long id) {
        orderRepo.deleteById(id);
    }


}
