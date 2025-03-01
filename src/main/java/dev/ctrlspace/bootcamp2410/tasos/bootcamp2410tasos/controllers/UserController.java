package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.LoginResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.OrderService;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();

        // Ensure role is not null before checking its value
        String role = authenticatedUser.getRole();
        if (role == null) {
            throw new SecurityException("User role is missing. Unauthorized access.");
        }

        if (role.equals("ROLE_ADMIN")) {
            return userService.getUsers();
        } else if (role.equals("ROLE_USER")) {
            return List.of(userService.getByEmail(authenticatedUser.getEmail()));
        } else {
            throw new SecurityException("Unauthorized access");
        }
    }


    @GetMapping("/users/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getByEmail(email);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(Authentication authentication) throws Exception {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new LoginResponse(user.getEmail(), user.getRole()));
    }

    @PostMapping("/users/register")
    public void createUser(@RequestBody User user) throws Exception {
        validateUserFields(user);
        userService.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User updatedUser, Authentication authentication) throws Exception {
        User authenticatedUser = (User) authentication.getPrincipal();

        if ("ROLE_USER".equals(authenticatedUser.getRole())) {
            // Regular users can only update their own info.
            if (!authenticatedUser.getEmail().equals(updatedUser.getEmail())) {
                throw new AccessDeniedException("Users can only update their own information.");
            }
            updatedUser.setId(authenticatedUser.getId());
            // Ensure the email is not changed by the user.
            updatedUser.setEmail(authenticatedUser.getEmail());
        } else if ("ROLE_ADMIN".equals(authenticatedUser.getRole())) {
            // Admins can update any user's details.
            // Since the payload does not include an ID, fetch the target user by email.
            User targetUser = userService.getByEmail(updatedUser.getEmail());
            if (targetUser == null) {
                throw new IllegalArgumentException("User not found for email: " + updatedUser.getEmail());
            }
            updatedUser.setId(targetUser.getId());
            // Prevent any role modifications by overwriting with the current role.
            updatedUser.setRole(targetUser.getRole());
        } else {
            throw new AccessDeniedException("Unauthorized role.");
        }

        // Validate the user fields before updating.
        validateUserFields(updatedUser);
        userService.updateUser(updatedUser);
        return userService.getByEmail(updatedUser.getEmail());
    }


    @DeleteMapping("/users/{email}")
    public void deleteUser(@PathVariable String email, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();

        // Admins can delete any user, users can only delete themselves
        if (!authenticatedUser.getRole().equals("ROLE_ADMIN") && !authenticatedUser.getEmail().equals(email)) {
            throw new AccessDeniedException("You are not authorized to delete this user.");
        }

        User userToDelete = userService.getByEmail(email);
        if (userToDelete == null) {
            throw new IllegalArgumentException("User not found.");
        }

        orderService.deleteOrdersByUserId(userToDelete.getId());
        userService.deleteUser(userToDelete);


    }


    public class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }


    private void validateUserFields(User user) throws ValidationException {
        if (user.getName() == null || user.getName().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getAddress() == null || user.getAddress().trim().isEmpty() ||
                user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
            throw new ValidationException("All fields (name, password, email, address, phone number) must be provided.");
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String message = ex.getMessage();

        // Handle specific exception messages
        if ("Email already exists.".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT); // 409 Conflict
        }

        if ("ID and Role must not be provided.".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        if (message != null && message.contains("All fields (name, password, email, address, phone number) must be provided")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Check if it's a security/access denied exception
        if (ex instanceof AccessDeniedException ||
                (message != null && message.contains("Access") && message.contains("denied")) ||
                (message != null && message.contains("forbidden"))) {
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN); // 403 Forbidden
        }

        // Default to Internal Server Error for unhandled exceptions
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
