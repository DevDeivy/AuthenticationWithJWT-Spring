package com.example.JWTauth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Lombok genera automáticamente los getters, setters, toString, equals y hashCode
@Data

// Lombok permite crear objetos con patrón Builder
@Builder

// Constructor sin argumentos (obligatorio para JPA)
@NoArgsConstructor

// Constructor con todos los argumentos (útil para pruebas o persistencia manual)
@AllArgsConstructor

// Marca esta clase como una entidad de JPA (tabla en la base de datos)
@Entity

// Define el nombre de la tabla como "users" y especifica que la columna username debe ser única
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})

 // Implementa UserDetails para que Spring Security pueda usar esta clase como modelo de usuario autenticado
public class User implements UserDetails {

    // Llave primaria de la tabla
    @Id
    @GeneratedValue
    Integer id;

    // Columna obligatoria (no puede ser nula en la base de datos)
    @Column(nullable = false)
    String userName;

    // atributos del usuario
    String firstName;
    String lastName;
    String password;
    String country;

    // Tipo de usuario (ej. ADMIN, USER), se asume que TypeUser es un enum
    TypeUser typeUser;

    // estos son metodos necesatios para que nuestra clase modelo funcione
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + typeUser.name()));
    }

    @Override
    public String getUsername() {
        return this.userName;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
