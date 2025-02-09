package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;

    }

    @PostMapping("/products")
    public DbProduct createProduct(@RequestBody DbProduct productToAdd, Authentication authentication) throws Exception {

        //check if user is admin
        if (!isUserAdmin(authentication)){
            throw new Exception("You are not authorized to add product");
        }

        validateCreateProduct(productToAdd);

        return productService.create(productToAdd);


    }


    @GetMapping("/products")
    public List<DbProduct> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{SKU}")
    public DbProduct getProductBySku(@PathVariable String SKU) throws Exception {
//        DbProduct product = productService.getProductBySku(SKU);
//        if (product == null) {
//            throw new Exception("Product not found with SKU: " + SKU);
//        }
        return productService.getProductBySku(SKU);
    }

    @PutMapping("/products/{SKU}")
    public void updateProduct(@PathVariable String SKU, @RequestBody DbProduct productToUpdate, Authentication authentication) throws Exception {

        //check if user is admin
        if (!isUserAdmin(authentication)){
            throw new Exception("You are not authorized to update a product");
        }

        validateUpdateProduct(SKU, productToUpdate);

        productService.updateProduct(productToUpdate);
    }

    @DeleteMapping("/products/{SKU}")
    public void deleteProduct(@PathVariable String SKU, Authentication authentication) throws Exception {
        //check if user is admin
        if (!isUserAdmin(authentication)){
            throw new Exception("You are not authorized to delete a product");
        }
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

    boolean isUserAdmin(Authentication authentication) {

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return authentication.getAuthorities().contains("ROLE_ADMIN");
    }



}
