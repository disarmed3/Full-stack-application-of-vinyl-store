package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.LoginResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.configurations.SecurityConfiguration;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Utility method to inject a custom principal (our User instance) into the request.
    private SecurityMockMvcRequestPostProcessors.RequestPostProcessor customUser(User customUser) {
        return request -> {
            request.setUserPrincipal(customUser);
            return request;
        };
    }

    @Nested
    class PostLoginTests {

        @Test
        public void testLogin_SimpleUser_ReturnsSuccessful() throws Exception {
            // Create a custom user instance representing a simple user.
            User simpleUser = new User();
            simpleUser.setEmail("tasos@ctrlspace.dev");
            simpleUser.setRole("ROLE_USER");
            // (You can also set a password if your code depends on it.)

            mockMvc.perform(post("/login")
                            .with(customUser(simpleUser)))  // Use our custom principal
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is("tasos@ctrlspace.dev")))
                    .andExpect(jsonPath("$.role", is("ROLE_USER")));
        }

        @Test
        public void testLogin_AdminUser_ReturnsSuccessful() throws Exception {
            User adminUser = new User();
            adminUser.setEmail("csekas@ctrlspace.dev");
            adminUser.setRole("ROLE_ADMIN");

            mockMvc.perform(post("/login")
                            .with(customUser(adminUser)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is("csekas@ctrlspace.dev")))
                    .andExpect(jsonPath("$.role", is("ROLE_ADMIN")));
        }

        @Test
        public void testLogin_InvalidCredentials_ReturnsUnauthorized() throws Exception {
            // In this scenario, we simulate a failure by not providing a principal.
            // (Your endpoint assumes a non-null principal; you may want to have a global exception handler.)
            mockMvc.perform(post("/login"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        public void testLogin_BlankCredentials_ReturnsUnauthorized() throws Exception {
            // Similarly, if blank credentials lead to no principal.
            mockMvc.perform(post("/login"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
