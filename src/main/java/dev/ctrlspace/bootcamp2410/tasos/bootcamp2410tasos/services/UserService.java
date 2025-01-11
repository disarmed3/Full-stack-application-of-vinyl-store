package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.services;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.DTO.LoginResponse;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private DBService dbService;
    private UserRepository userRepository;

    public UserService(DBService dbService, UserRepository userRepository) {

        this.dbService = dbService;
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getByEmail(String email)  {

        return userRepository.findByEmail(email);

    }

   public void addUser(User user) throws Exception {
        if (user.getId() != null) {
            throw  new Exception("Id must not be given");
        }
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new Exception("Email already exists");
        }
        userRepository.save(user);
    }

    public LoginResponse login(String email, String password) throws Exception {
        // Authenticate user with provided email and password
        User user = userRepository.loginUser(email, password);

        if (user == null) {
            throw new Exception("User not found");
        }

        // Return a LoginResponse with the email and role
        return new LoginResponse(user.getEmail(), user.getRole());
    }

    public void updateUser(User user, User authenticatedUser) throws Exception {


        if (StringUtils.isNotBlank(user.getEmail())) {
            authenticatedUser.setEmail(user.getEmail());
        }
        if (StringUtils.isNotBlank(user.getPassword())) {
            authenticatedUser.setPassword(user.getPassword());
        }
        if (StringUtils.isNotBlank(user.getName())) {
            authenticatedUser.setName(user.getName());
        }
        if (StringUtils.isNotBlank(user.getAddress())) {
            authenticatedUser.setAddress(user.getAddress());
        }
        if (StringUtils.isNotBlank(user.getPhoneNumber())) {
            authenticatedUser.setPhoneNumber(user.getPhoneNumber());
        }

        userRepository.save(authenticatedUser);

    }

    public void deleteUser(User authenticatedUser) {

        userRepository.delete(authenticatedUser);
                }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email);
        return user;
    }
}
