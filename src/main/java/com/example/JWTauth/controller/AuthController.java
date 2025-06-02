package com.example.JWTauth.controller;

import com.example.JWTauth.requests.LoginRequest;
import com.example.JWTauth.requests.RegisterRequest;
import com.example.JWTauth.requests.ResponseToken;
import com.example.JWTauth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//definimos los endpoints, getters, setters y constructores (@RequiredArgsConstructor)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    //inyectamos el AuthService para tener acceso a sus metodos
    private final AuthService authService;

    //definimos el metodo HTTP y su endpoint
    @PostMapping("/login")
    public ResponseEntity<ResponseToken> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity
                .ok(authService.login(request));
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseToken> register(@RequestBody RegisterRequest request){
        return ResponseEntity
                .ok(authService.register(request));
    }
}
