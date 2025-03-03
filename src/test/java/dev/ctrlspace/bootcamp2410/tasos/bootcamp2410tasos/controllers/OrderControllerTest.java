package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.configurations.SecurityConfiguration;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.OrderService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@Import(SecurityConfiguration.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Nested
    class testGetAllOrders {

        
    }

}
