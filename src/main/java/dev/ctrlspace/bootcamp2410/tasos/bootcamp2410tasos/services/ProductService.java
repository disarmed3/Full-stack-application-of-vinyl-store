package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.ProductCart;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final DBService dbService;
    private final ProductRepository productRepository;

    public ProductService(DBService dbService, ProductRepository productRepository) {

        this.dbService = dbService;
        this.productRepository = productRepository;
    }

    public List<DbProduct> getAllProducts() {
        return productRepository.findAll();
    }

    public DbProduct getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public DbProduct create(DbProduct productToAdd) throws Exception {
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
    public void updateProduct(DbProduct productToUpdate) {

        var existingProduct = getProductBySku(productToUpdate.getSku());
        existingProduct.setName(productToUpdate.getName());
        existingProduct.setPrice(productToUpdate.getPrice());
        existingProduct.setStock(productToUpdate.getStock());
        existingProduct.setDescription(productToUpdate.getDescription());

        productRepository.save(existingProduct);

    }

    public void returnProductsToStock(List<dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.ProductCart> cart) {
        for (dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.ProductCart productCart : cart) {
            DbProduct stockProduct = getProductBySku(productCart.getProduct().getSku());
            stockProduct.setStock(stockProduct.getStock() + productCart.getQuantity());
        }
    }


    public void removeProductsFromStock(List<dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.ProductCart> cart) {
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