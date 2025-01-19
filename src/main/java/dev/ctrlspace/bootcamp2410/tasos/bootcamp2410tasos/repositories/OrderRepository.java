package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories;


import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.user.email = :email")
    List<Order> findByEmail(@Param("email") String email);

    Optional<Order> findByOrderNumber(String orderNumber);




}
