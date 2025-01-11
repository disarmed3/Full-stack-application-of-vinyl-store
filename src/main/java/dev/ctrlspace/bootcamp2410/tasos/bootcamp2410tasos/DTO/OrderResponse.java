package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.Order;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.ProductCart;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String orderStatus;
    private Instant createAt;
    private String userName;
    private String userEmail;
    private List<ProductCartResponse> productCarts;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.orderStatus = order.getOrderStatus();
        this.createAt = order.getCreateAt();
        this.userName = order.getUser().getName();
        this.userEmail = order.getUser().getEmail();
        this.productCarts = order.getProductCarts().stream()
                .map(ProductCartResponse::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class ProductCartResponse {
        private String name;
        private String description;
        private String sku;
        private double quantity;
        private double price;

        public ProductCartResponse(ProductCart productCart) {
            this.name = productCart.getProduct().getName();
            this.description = productCart.getProduct().getDescription();
            this.sku = productCart.getProduct().getSku();
            this.quantity = productCart.getQuantity();
            this.price = productCart.getProduct().getPrice();
        }
    }
}





