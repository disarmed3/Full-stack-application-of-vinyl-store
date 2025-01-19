package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.OrderResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.Order;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.ProductCart;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.OrderRepository;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.ProductCartRepository;
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
    private OrderRepository orderRepository;
    private UserService userService;
    private UserRepository userRepository;
    private ProductService productService;
    private ProductCartRepository productCartRepository;

    private List<String> validStatuses = Arrays.asList("NEW", "IN_PROGRESS", "COMPLETED", "CANCELLED");


    public OrderService(UserService userService,
                        ProductService productService, OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, ProductCartRepository productCartRepository) {

        this.userService = userService;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productCartRepository = productCartRepository;
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

        order= orderRepository.save(order);
        productCartRepository.saveAll(order.getProductCarts());
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


    public Order getOrderByOrderNumber(String orderNumber) throws Exception {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new Exception("Order not found with orderNumber: " + orderNumber));
    }


    public void deleteOrder(String orderNumber) throws Exception {

        Order order = getOrderByOrderNumber(orderNumber);

        if(order == null) {
            throw new Exception("Order not found");
        }

        productCartRepository.deleteAll(order.getProductCarts());
        orderRepository.delete(order);

    }

    public Order updateOrderStatus(String orderNumber, String newStatus) throws Exception {
        // Fetch the existing order by orderNumber
        Order existingOrder = getOrderByOrderNumber(orderNumber);

        // Validate the new order status
        if (!validStatuses.contains(newStatus)) {
            throw new Exception("Invalid order status: " + newStatus);
        }

        // Update order status and timestamp
        existingOrder.setOrderStatus(newStatus);
        existingOrder.setUpdateAt(Instant.now());

        // Save the updated order
        return orderRepository.save(existingOrder);
    }




}