package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.controllers;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.LoginResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.OrderService;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(){

        return userService.getUsers();
    }

    @GetMapping("/users/{email}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByEmail(@PathVariable String email) throws Exception {

        return userService.getByEmail(email);

    }

    @PostMapping("/login")
    public LoginResponse login(Authentication authentication) throws Exception {
        String email = ((User) authentication.getPrincipal()).getEmail();
        String password = ((User) authentication.getPrincipal()).getPassword(); // Assuming password is available via authentication

        return userService.login(email, password); // Call the login method that returns LoginResponse
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody User user) throws Exception {

        if (user.getName().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty() ){
            throw new Exception("Essential data is blank");
        }

        userService.addUser(user);
    }

//    @PutMapping("/users")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void updateUser(@RequestBody User user, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception {
//
//        User authenticatedUser = userService.login(email, password);
//
//        userService.updateUser(user, authenticatedUser);
//
//    }
//
//    @DeleteMapping("/users/{email}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void deleteUser(@PathVariable String email, @RequestHeader("password") String password) throws Exception {
//
//
//        User authenticatedUser = userService.login(email, password);
//
//        userService.deleteUser(authenticatedUser);
//    }
}
