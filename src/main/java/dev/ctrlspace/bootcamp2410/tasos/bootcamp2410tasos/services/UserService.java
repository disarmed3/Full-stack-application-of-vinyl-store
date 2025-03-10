package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.LoginResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addUser(User user) throws Exception {
        if (user.getId() != null || user.getRole() != null) {
            throw new Exception("ID and Role must not be provided.");
        }
        // Convert email to lowercase
        user.setEmail(user.getEmail().toLowerCase());

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email already exists.");
        }

        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    public LoginResponse login(String email, String password) throws Exception {
        User user = userRepository.loginUser(email, password);
        if (user == null) {
            throw new Exception("User not found");
        }
        return new LoginResponse(user.getEmail(), user.getRole());
    }

    public void updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId()).orElse(null);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        //overwrites role to prevent modification
        updatedUser.setRole(existingUser.getRole());

        existingUser.setName(updatedUser.getName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        userRepository.save(existingUser);
    }

    public void deleteUser(User authenticatedUser) {
        userRepository.delete(authenticatedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        return user;
    }
}
