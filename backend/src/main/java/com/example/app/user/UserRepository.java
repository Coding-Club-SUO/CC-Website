package com.example.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<User, String>{
    //Leave this empty unless you want to add more custom CRUD operations
}
