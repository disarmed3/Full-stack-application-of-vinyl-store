package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.exceptions.ProductExceptions.ProductValidationException;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.ProductService;
import org.springframework.http.HttpStatus;
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
    public DbProduct createProduct(@RequestBody DbProduct productToAdd) throws ProductValidationException {

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
    public void updateProduct(@PathVariable String SKU, @RequestBody DbProduct productToUpdate) throws ProductValidationException{

        validateUpdateProduct(SKU, productToUpdate);

        productService.updateProduct(productToUpdate);
    }

    @DeleteMapping("/products/{SKU}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable String SKU) throws Exception {

        productService.deleteProduct(SKU);
    }

    private static void validateCreateProduct(DbProduct productToAdd) throws ProductValidationException {
        if (productToAdd.getId() != null || productToAdd.getSku() != null) {
            throw new ProductValidationException("ID and SKU must be empty");
        }
        validateProductBasicFields(productToAdd);
    }

    private static void validateUpdateProduct(String sku, DbProduct productToUpdate) throws ProductValidationException {
        // validate SKU
        if (!sku.equals(productToUpdate.getSku())) {
            throw new ProductValidationException("SKU does not match");
        }
        validateProductBasicFields(productToUpdate);
    }

    private static void validateProductBasicFields(DbProduct productToUpdate) throws ProductValidationException {
        // validate name, description non empty, price > 0, stock >= 0
        if (productToUpdate.getName() == null || productToUpdate.getName().isEmpty()) {
            throw new ProductValidationException("Name cannot be empty");
        }
        if (productToUpdate.getDescription() == null || productToUpdate.getDescription().isEmpty()) {
            throw new ProductValidationException("Description cannot be empty");
        }
        if (productToUpdate.getPrice() == null || productToUpdate.getPrice() <= 0) {
            throw new ProductValidationException("Price must be greater than 0");
        }
        if (productToUpdate.getStock() == null || productToUpdate.getStock() < 0) {
            throw new ProductValidationException("Stock must be greater than or equal to 0");
        }
    }



    @ExceptionHandler(ProductValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(ProductValidationException ex) {
        return ex.getMessage();
    }



}
