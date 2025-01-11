package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderNumber;
    private String orderDate;
    private String orderStatus;

    private User user;

    private List<ProductCart> products;



}