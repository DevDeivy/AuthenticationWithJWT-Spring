package com.example.JWTauth.securityControllers;

import com.example.JWTauth.JWTFilter.JWTauthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration

//esto es para habilitar la configuración personalizada de Spring Security
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityToControllers {

    //inyectamos nuestras dependencias
    private final JWTauthFilter jwTauthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        //esto lo que hace es desactivar el filtro CSRF dado que no se usan las cookies en este ejemplo,
        //y permitir todos request a la ruta que empiece por /auth y cualquier otra request debe ser autenticado
        return httpSecurity.
                csrf(csrf->
                        csrf.disable())
                .authorizeHttpRequests(
                authRequest -> authRequest
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
        ).sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwTauthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }
}
