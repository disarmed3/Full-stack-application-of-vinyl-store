package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;

    }

    @PostMapping("/products")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DbProduct createProduct(@RequestBody DbProduct productToAdd) throws Exception {

        validateCreateProduct(productToAdd);

        return productService.create(productToAdd);
    }


    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<DbProduct> getProducts() {
        return productService.getAllProducts();
    }


    @GetMapping("/products/{SKU}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public DbProduct getProductBySku(@PathVariable String SKU) {

        return productService.getProductBySku(SKU);
    }

    @PutMapping("/products/{SKU}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateProduct(@PathVariable String SKU, @RequestBody DbProduct productToUpdate) throws Exception {

        validateUpdateProduct(SKU, productToUpdate);

        productService.updateProduct(productToUpdate);
    }

    @DeleteMapping("/products/{SKU}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable String SKU) throws Exception {

        productService.deleteProduct(SKU);
    }

    private static void validateCreateProduct(DbProduct productToAdd) throws Exception {
        if (productToAdd.getId() != null || productToAdd.getSku() != null) {
            throw new Exception("ID and SKU must be empty");
        }
        validateProductBasicFields(productToAdd);
    }

    private static void validateUpdateProduct(String sku, DbProduct productToUpdate) throws Exception {
        // validate SKU
        if (!sku.equals(productToUpdate.getSku())) {
            throw new Exception("SKU does not match");
        }
        validateProductBasicFields(productToUpdate);
    }

    private static void validateProductBasicFields(DbProduct productToUpdate) throws Exception {
        // validate name, description non empty, price > 0, stock >= 0
        if (productToUpdate.getName() == null || productToUpdate.getName().isEmpty()) {
            throw new Exception("Name cannot be empty");
        }
        if (productToUpdate.getDescription() == null || productToUpdate.getDescription().isEmpty()) {
            throw new Exception("Description cannot be empty");
        }
        if (productToUpdate.getPrice() == null || productToUpdate.getPrice() <= 0) {
            throw new Exception("Price must be greater than 0");
        }
        if (productToUpdate.getStock() == null || productToUpdate.getStock() < 0) {
            throw new Exception("Stock must be greater than or equal to 0");
        }
    }

//    boolean isUserAdmin(Authentication authentication) {
//
//        for (GrantedAuthority authority : authentication.getAuthorities()) {
//            if (authority.getAuthority().equals("ROLE_ADMIN")) {
//                return true;
//            }
//        }
//        return authentication.getAuthorities().contains("ROLE_ADMIN");
//    }



}
