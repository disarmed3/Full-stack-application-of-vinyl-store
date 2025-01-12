package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories;


import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.DbProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<DbProduct, Long> {


    DbProduct findBySku(String sku);

    @Query(value = "SELECT * " +
            "FROM products " +
            "ORDER BY CAST(SUBSTRING(sku FROM '[0-9]+$') AS INTEGER) DESC " +
            "LIMIT 1",
            nativeQuery = true)
    DbProduct findByMaxSku();



}
