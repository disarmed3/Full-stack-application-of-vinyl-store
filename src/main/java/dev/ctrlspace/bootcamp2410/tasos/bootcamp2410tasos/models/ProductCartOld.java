package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCartOld {

    private ProductOld product;
    private Double cartQuantity;
}
