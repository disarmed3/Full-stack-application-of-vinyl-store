package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.repositories;

import dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE email = ?1 AND password = ?2")
    User loginUser(String email, String password);

}
