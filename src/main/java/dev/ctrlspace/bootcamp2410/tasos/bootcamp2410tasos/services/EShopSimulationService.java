package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.Order;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.dbentities.ProductCart;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class EShopSimulationService {

    private DBService database;
    private OrderService orderService;

    public EShopSimulationService(DBService database, OrderService orderService) {
        this.database = database;
        this.orderService = orderService;
    }


    public void run() throws Exception {


        Random r = new Random();

        for(int day = 0 ; day < 10 ; day++) {

            System.out.println("Gooooood morning! It's day " + day);

            Integer numberOfOrders = r.nextInt(10) + 1;

            System.out.println("We have " + numberOfOrders + " orders today!");

            for (int i = 0; i < numberOfOrders; i++) {
                Order o = createRandomOrder(database);
            }

        }

        System.out.println("We have " + database.countOrders() + " orders in total!");

    }


    public Order createRandomOrder(DBService newDatabase) throws Exception {

        Random r = new Random();
        // enas tipos mpainei sto magazi
        User u = newDatabase.getUserByIndex(r.nextInt(newDatabase.countUsers()));

        // pairnei ena adio kala8i
        List<ProductCart> cart = new ArrayList<>();

        // skeftetai oti tha agorasei px, 4 diaforetika pramata
        Integer numberOfProducts = r.nextInt(5) + 1;

        // dialegei 4 pragmata
        for(int i = 0 ; i < numberOfProducts ; i++) {
            DbProduct p = newDatabase.getProductByIndex(r.nextInt(newDatabase.countProducts()));
            Integer quantity = r.nextInt(5) + 1;

            ProductCart pc = new ProductCart();
            pc.setProduct(p);
            pc.setQuantity(Double.valueOf(quantity));
            cart.add(pc);
        }


        //bazei thn paragkelia sto magazi
        Order o = orderService.createNewOrder(u, cart);


        return o;


    }





}
