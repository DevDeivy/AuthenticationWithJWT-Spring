package com.example.JWTauth.securityControllers;

import com.example.JWTauth.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Indica que esta clase contiene beans de configuración
@Configuration

@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    // Este bean expone el AuthenticationManager que maneja la autenticación general en Spring Security
    @Bean

    // Obtener el gestor de autenticación ya configurado
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); 
    }

    // Este bean define el proveedor de autenticación usando DAO (acceso a base de datos)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        // Indica cómo cargar usuarios desde la base de datos
        authenticationProvider.setUserDetailsService(userDetailService());

        // Indica cómo se deben verificar las contraseñas (en este caso, usando BCrypt)
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    // Bean que indica el algoritmo de encriptación para las contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean que le dice a Spring Security cómo cargar un usuario a partir de su username
    @Bean
    // Si no encuentra al usuario en la base de datos, lanza excepción
    public UserDetailsService userDetailService() {
        return username -> userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); 
    }
}
