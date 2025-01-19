package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders", schema = "public")
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "productCarts")
    private List<ProductCart> productCarts;

    @Basic
    @Column(name = "order_number", nullable = false, length = -1)
    private String orderNumber;
    @Basic
    @Column(name = "order_status", nullable = false, length = -1)
    private String orderStatus;
    @Basic
    @Column(name = "create_at", nullable = false)
    private Instant createAt;
    @Basic
    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ProductCart> getProductCarts() {
        return productCarts;
    }

    public void setProductCarts(List<ProductCart> productCarts) {
        this.productCarts = productCarts;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(user, order.user) && Objects.equals(productCarts, order.productCarts) && Objects.equals(orderNumber, order.orderNumber) && Objects.equals(orderStatus, order.orderStatus) && Objects.equals(createAt, order.createAt) && Objects.equals(updateAt, order.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, productCarts, orderNumber, orderStatus, createAt, updateAt);
    }
}
