package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;


import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.OrderResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.Order;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.OrderService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {


    private OrderService orderService;


    public OrderController(OrderService orderService) {

        this.orderService = orderService;

    }


    @GetMapping("/orders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getAllOrders(){

        return orderService.getAllOrders();
    }

    @GetMapping("/orders/user/{email}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<OrderResponse> getOrdersByEmail(@PathVariable String email, Authentication authentication) {
        User loginUser = (User) authentication.getPrincipal();
        if (!loginUser.getEmail().equals(email)) {
            throw new AccessDeniedException("You are not authorized to view these orders.");
        }

        return orderService.getOrdersByEmail(email);
    }

    @GetMapping("/orders/{orderNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Order getOrderById(@PathVariable String orderNumber) throws Exception {

        return orderService.getOrderByOrderNumber(orderNumber);
    }


    @PostMapping("/orders/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Order createOrder(@RequestBody Order order) throws Exception {


        if (order.getOrderNumber() != null ) {
            throw new Exception("Order Number should be null");
        }

        return orderService.createNewOrder(order.getUser(), order.getProductCarts());

    }

    @DeleteMapping("/orders/{orderNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteOrder(@PathVariable String orderNumber) throws Exception {

        orderService.deleteOrder(orderNumber);
    }

    @PutMapping("/orders/{orderNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Order updateOrderStatus(@PathVariable String orderNumber, @RequestBody Map<String, String> requestBody) throws Exception {
        String newStatus = requestBody.get("orderStatus");

        return orderService.updateOrderStatus(orderNumber, newStatus);
    }



}
