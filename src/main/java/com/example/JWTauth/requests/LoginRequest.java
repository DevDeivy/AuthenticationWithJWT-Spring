package com.example.JWTauth.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

//esta clase tiene como funcionalidad tener los atributos requeridos con los que se interactuarán
public class LoginRequest {
    String username;
    String password;
}
