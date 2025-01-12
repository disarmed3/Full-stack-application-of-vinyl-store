package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.OrderResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.Order;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.ProductCart;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.OrderRepository;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.ProductRepository;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private DBService dbService;
    private OrderRepository orderRepository;
    private UserService userService;
    private UserRepository userRepository;
    private ProductService productService;

    private List<String> validStatuses = Arrays.asList("NEW", "IN_PROGRESS", "COMPLETED", "CANCELLED");
    private List<String> updatableStatuses = Arrays.asList("NEW", "IN_PROGRESS");

    public OrderService(DBService dbService,
                        UserService userService,
                        ProductService productService, OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.dbService = dbService;
        this.userService = userService;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }



    public Order createNewOrder(User user, List<ProductCart> cart) throws Exception {

        Order order = new Order();


        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null) {
            throw new Exception("User not found");
        }

        Map<DbProduct, Double> uniqueProducts = getUniqueProducts(cart);

        validateProductAvailable(uniqueProducts);

        productService.removeProductsFromStock(cart);

        order.setOrderNumber("ORD-" + UUID.randomUUID());
        order.setCreateAt(Instant.now());
        order.setUpdateAt(Instant.now());
        order.setOrderStatus("NEW");
        order.setUser(existingUser);

        // Update the cart with full product details
        for (ProductCart productCart : cart) {
            DbProduct fullProduct = productRepository.findBySku(productCart.getProduct().getSku());
            if (fullProduct == null) {
                throw new Exception("Product with SKU " + productCart.getProduct().getSku() + " not found");
            }
            productCart.setProduct(fullProduct); // Set the full product details
            productCart.setOrder(order);        // Associate the cart with the order
        }

        order.setProductCarts(cart);

        orderRepository.save(order);
        System.out.println("Order created successfully with order number: " + order.getOrderNumber());

        return order;
    }

    private static Map<DbProduct, Double> getUniqueProducts(List<ProductCart> cart) {
        Map<DbProduct, Double> uniqueProducts = new HashMap<>();
        for (ProductCart productCart : cart) {
            if (!uniqueProducts.containsKey(productCart.getProduct())) {
                uniqueProducts.put(productCart.getProduct(), productCart.getQuantity());
            } else {
                uniqueProducts.put(productCart.getProduct(), uniqueProducts.get(productCart.getProduct()) + productCart.getQuantity());
            }
        }
        return uniqueProducts;
    }

    /**
     * Gets a Map of products and their quantities and validates if the products are available in stock
     *
     * @param uniqueProducts
     * @throws Exception
     */

    private void validateProductAvailable(Map<DbProduct, Double> uniqueProducts) throws Exception {
        List<String> errors = new ArrayList<>();
        for (Map.Entry<DbProduct, Double> entry : uniqueProducts.entrySet()) {

            DbProduct toBeOrderedProduct = entry.getKey();
            Double toBeOrderedQuantity = entry.getValue();


            DbProduct existingProduct = productRepository.findBySku(toBeOrderedProduct.getSku());
            if (existingProduct == null) {
                errors.add("Product with Name " + toBeOrderedProduct.getName() + " not found");
                continue;
            }

            if (existingProduct.getStock().doubleValue() < toBeOrderedQuantity) {
                errors.add("Not enough quantity for product with Name " + toBeOrderedProduct.getName());
                continue;
            }

        }


        if (!errors.isEmpty()) {
            String errorMessage = "Errors occurred while creating order:";
            for (String error : errors) {
                errorMessage += "\n" + error;
            }
            throw new Exception(errorMessage);
        }
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByEmail(String email) {
        List<Order> orders = orderRepository.findByEmail(email);
        return orders.stream().map(OrderResponse::new).collect(Collectors.toList());
    }


    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderNumber().equals(orderNumber))
                .findFirst()
                .orElse(null);
    }

    public void deleteOrder(String orderNumber, User authenticatedUser) throws Exception {

        Order order = getOrderByOrderNumber(orderNumber);

        if(order == null) {
            throw new Exception("Order not found");
        }

        if (!authenticatedUser.getEmail().equals(order.getUser().getEmail())) {
            throw new Exception("You are not authorized to delete this order");
        }


        //dbService.deleteOrder(order);
        orderRepository.delete(order);
    }

    public Order updateOrder(String orderNumber, Order updateOrder, User authenticatedUser) throws Exception {

        Order existingOrder = getOrderByOrderNumber(orderNumber);

        if (!validStatuses.contains(updateOrder.getOrderStatus())) {
            throw new Exception("Invalid order status");
        }

        if(updatableStatuses.contains(updateOrder.getOrderStatus())) {
            existingOrder.setOrderStatus(updateOrder.getOrderStatus());
        }

        User newUser = userService.getByEmail(updateOrder.getUser().getEmail());
        existingOrder.setUser(newUser);


        Map<DbProduct, Double> uniqueProducts = getUniqueProducts(updateOrder.getProductCarts());

        validateProductAvailable(uniqueProducts);

        productService.returnProductsToStock(existingOrder.getProductCarts());
        productService.removeProductsFromStock(updateOrder.getProductCarts());

        List<ProductCart> cart = updateOrder.getProductCarts();
        for (ProductCart productCart : updateOrder.getProductCarts()) {
            DbProduct existingProduct = dbService.getProductBySKU(productCart.getProduct().getSku());
            productCart.setProduct(existingProduct);
        }

        existingOrder.setProductCarts(updateOrder.getProductCarts());

        return existingOrder;
    }


}