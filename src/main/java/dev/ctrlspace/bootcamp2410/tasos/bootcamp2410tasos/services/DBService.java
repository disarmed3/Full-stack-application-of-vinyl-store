package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.Order;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DBService {

    private List<User> allUsers = new ArrayList<>();
    private List<DbProduct> allProducts = new ArrayList<>();
    private List<Order> allOrders = new ArrayList<>();

    public DBService() {

        //initData();
    }

    public void insertUser(User user)  {


//        if (allUsers.contains(user)) {
//            return;
//        }

        User existingUser = getUserByEmail(user.getEmail());
        if (existingUser != null) {
            return;
        }

        allUsers.add(user);
    }

    public void insertProduct(DbProduct product){
        if (getProductBySKU(product.getSku()) != null) {
            return;
        }
        allProducts.add(product);
    }

    public void insertOrder(Order order){


        allOrders.add(order);
    }


    public User getUserByIndex(int index){
        return allUsers.get(index);
    }

    public User getUserByEmail(String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    public DbProduct getProductByIndex(int index){
        return allProducts.get(index);
    }

    public DbProduct getProductBySKU(String sku) {
        for (DbProduct product : allProducts) {
            if (product.getSku().equals(sku)) {
                return product;
            }
        }
        return null;
    }

    public List<DbProduct> getAllProducts() {
        return allProducts;
    }

    public List<Order> getAllOrders(){
        return allOrders;
    }

    public List<User> getUsers() { return allUsers;    }

    public int countProducts(){
        return allProducts.size();
    }

    public int countOrders(){
        return allOrders.size();
    }

    public int countUsers(){
        return allUsers.size();
    }

    public void deleteOrder(Order order){
        allOrders.remove(order);
    }

    public void deleteUser(User user) {
        allUsers.remove(user);
    }

    public void deleteProduct(DbProduct product) {
        allProducts.remove(product);
    }

//    public void initData() {
//        User u1 = new User("Chris Sekas", "csekas@ctrlspace.dev", "1234", "6988888888", "Tzortz 9 Athens");
//        User u2 = new User("Tasos", "tasos@ctrlspace.dev", "123456", "6989999999", "Thessaloniki str.");
//
//        Product p1 = new Product("SKU-123", "Laptop", "Acer Aspire 5", 500.0, 10D);
//        Product p2 = new Product("SKU-124", "Smartphone", "Samsung Galaxy S21", 1000.0, 5D);
//        Product p3 = new Product("SKU-125", "Tablet", "Apple iPad Pro", 800.0, 3D);
//        Product p4 = new Product("SKU-126", "Smartwatch", "Apple Watch Series 6", 400.0, 7D);
//
//
//        this.insertUser(u1);
//        this.insertUser(u2);
//
//        this.insertProduct(p1);
//        this.insertProduct(p2);
//        this.insertProduct(p3);
////        this.insertProduct(p4);
//
//    }

}
