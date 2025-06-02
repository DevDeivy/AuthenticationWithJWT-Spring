package com.example.JWTauth.services;

import com.example.JWTauth.models.TypeUser;
import com.example.JWTauth.models.User;
import com.example.JWTauth.repository.UserRepository;
import com.example.JWTauth.requests.LoginRequest;
import com.example.JWTauth.requests.RegisterRequest;
import com.example.JWTauth.requests.ResponseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

//genera los constructores, setters y getters
@RequiredArgsConstructor
public class AuthService {

    //inyectamos las depencencias
    private final UserRepository userRepository;
    private final JWTservice jwTservice;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

        // Método que se encarga del proceso de login autenticand el usuario (nombre y contraseña)
    public ResponseToken login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        // Busca el usuario en la base de datos
        UserDetails user = userRepository.findByUserName(request.getUsername()).orElseThrow();

        // Genera un token JWT para ese usuario
        String token = jwTservice.getToken(user);

        // Retorna el token dentro de un objeto ResponseToken
        return ResponseToken.builder()
                .token(token)
                .build();
    }

    // Método que se encarga del proceso de registro
    public ResponseToken register(RegisterRequest request){
        User user = User.builder().
                userName(request.getUsername())
                .password(passwordEncoder.encode( request.getPassword()))
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .country(request.getCountry())
                .typeUser(TypeUser.USER).build();

        userRepository.save(user);
        return ResponseToken.builder()
                .token(jwTservice.getToken(user)).build();
    }
}
