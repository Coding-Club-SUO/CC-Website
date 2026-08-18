package com.example.app.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.user.entity.User;

@Repository
public interface  UserRepository extends JpaRepository<User, String>{
    //Leave this empty unless you want to add more custom CRUD operations

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
