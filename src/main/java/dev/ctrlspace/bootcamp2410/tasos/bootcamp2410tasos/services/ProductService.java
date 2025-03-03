package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.exceptions.ProductExceptions.ProductValidationException;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.ProductCart;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    public ProductService(DBService dbService, ProductRepository productRepository) {


        this.productRepository = productRepository;
    }

    public List<DbProduct> getAllProducts() {
        return productRepository.findAll();
    }

    public DbProduct getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public DbProduct create(DbProduct productToAdd) throws ProductValidationException {
        // get product with max id
        // create SKU by incrementing max id and padding with 0s to 6 digits
        DbProduct productWithMaxId = productRepository.findByMaxSku();

        if (productWithMaxId == null) {
            productToAdd.setSku("SKU-000001");
        } else {
            String maxSku = productWithMaxId.getSku();
            String[] skuParts = maxSku.split("-");
            int maxId = Integer.parseInt(skuParts[1]);
            maxId++;
            String newSku = "SKU-" + String.format("%06d", maxId);
            productToAdd.setSku(newSku);
        }
        return productRepository.save(productToAdd);

    }
    public void updateProduct(DbProduct productToUpdate) throws ProductValidationException{

        var existingProduct = getProductBySku(productToUpdate.getSku());

        if (existingProduct == null) {
            throw new ProductValidationException("Product not found");
        }


        existingProduct.setName(productToUpdate.getName());
        existingProduct.setPrice(productToUpdate.getPrice());
        existingProduct.setStock(productToUpdate.getStock());
        existingProduct.setDescription(productToUpdate.getDescription());

        productRepository.save(existingProduct);

    }


    public void removeProductsFromStock(List<ProductCart> cart) {
        for (ProductCart productCart : cart) {
            DbProduct stockProduct = getProductBySku(productCart.getProduct().getSku());
            stockProduct.setStock(stockProduct.getStock() - productCart.getQuantity());

        }
    }


    public void deleteProduct(String sku) throws Exception {

        DbProduct productToDelete = getProductBySku(sku);

        if (productToDelete == null) {
            throw new Exception("Product not found");
        }

        productRepository.delete(productToDelete);
    }
}