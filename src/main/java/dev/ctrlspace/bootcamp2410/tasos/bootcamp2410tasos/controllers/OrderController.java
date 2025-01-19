package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;


import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.OrderResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.Order;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.EShopSimulationService;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.OrderService;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    private UserService userService;
    private EShopSimulationService eShopSimulationService;
    private OrderService orderService;


    public OrderController(OrderService orderService,
                           UserService userService,
                           EShopSimulationService eShopSimulationService) {
        this.eShopSimulationService = eShopSimulationService;
        this.orderService = orderService;
        this.userService = userService;
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
    public Order getOrderById(@PathVariable String orderNumber) throws Exception {

        return orderService.getOrderByOrderNumber(orderNumber);
    }


    @PostMapping("/orders/user")
    public Order createOrder(@RequestBody Order order) throws Exception {


        if (order.getOrderNumber() != null ) {
            throw new Exception("Order Number should be null");
        }

        return orderService.createNewOrder(order.getUser(), order.getProductCarts());

    }

    @DeleteMapping("/orders/{orderNumber}")
    public void deleteOrder(@PathVariable String orderNumber) throws Exception {

        orderService.deleteOrder(orderNumber);
    }

    @PutMapping("/orders/{orderNumber}")
    public Order updateOrderStatus(@PathVariable String orderNumber, @RequestBody Map<String, String> requestBody) throws Exception {
        String newStatus = requestBody.get("orderStatus");

        return orderService.updateOrderStatus(orderNumber, newStatus);
    }



}
