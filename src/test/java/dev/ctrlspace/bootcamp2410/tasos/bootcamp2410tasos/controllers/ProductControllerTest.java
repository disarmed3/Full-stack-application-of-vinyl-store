package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.configurations.SecurityConfiguration;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.ProductService;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.DbProduct;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(SecurityConfiguration.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserService userService;

    @Nested
    class CreateProductTests {

        @Test
        public void createProduct_asAdmin_createsNewProduct() throws Exception {

            DbProduct inputProduct = new DbProduct();
            inputProduct.setName("Test Vinyl");
            inputProduct.setDescription("A test vinyl record");
            inputProduct.setPrice(29.99);
            inputProduct.setStock(10.0);

            DbProduct savedProduct = new DbProduct();
            savedProduct.setId(1L);
            savedProduct.setSku("SKU-000001");
            savedProduct.setName("Test Vinyl");
            savedProduct.setDescription("A test vinyl record");
            savedProduct.setPrice(29.99);
            savedProduct.setStock(10.0);

            when(productService.create(any(DbProduct.class))).thenReturn(savedProduct);


            mockMvc.perform(post("/products")
                            .with(user("admin").roles("ADMIN"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Test Vinyl\",\"description\":\"A test vinyl record\",\"price\":29.99,\"stock\":10.0}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.sku", is("SKU-000001")))
                    .andExpect(jsonPath("$.name", is("Test Vinyl")))
                    .andExpect(jsonPath("$.description", is("A test vinyl record")))
                    .andExpect(jsonPath("$.price", is(29.99)))
                    .andExpect(jsonPath("$.stock", is(10.0)));

            verify(productService, times(1)).create(any(DbProduct.class));
        }
    }

    @Test
    public void createProduct_asUser_returnsForbidden() throws Exception {


        DbProduct inputProduct = new DbProduct();
        inputProduct.setName("Test Vinyl");
        inputProduct.setDescription("A test vinyl record");
        inputProduct.setPrice(29.99);
        inputProduct.setStock(10.0);

        mockMvc.perform(post("/products")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Vinyl\",\"description\":\"A test vinyl record\",\"price\":29.99,\"stock\":10.0}"))
                .andExpect(status().isForbidden());

        verify(productService, never()).create(any(DbProduct.class));

    }

    @Test
    public void createProduct_withIDProvided_returnsBadRequest() throws Exception {

        mockMvc.perform(post("/products")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"Test Vinyl\",\"description\":\"A test vinyl record\",\"price\":29.99,\"stock\":10.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("ID and SKU must be empty")));
    }
}


