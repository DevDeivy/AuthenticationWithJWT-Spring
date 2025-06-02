package com.example.JWTauth.repository;

import com.example.JWTauth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Esta interfaz actúa como el repositorio para acceder a los datos de los usuarios (tabla users),
//al heredar de JpaRepository, podemos acceder a los métodos CRUD (Create, Read, Update, Delete)
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String username);
}
