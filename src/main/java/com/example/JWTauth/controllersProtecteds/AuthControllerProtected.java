package com.example.JWTauth.controllersProtecteds;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//es lo mismo que el controlador anterior pero este es para usuarios autorizados (con token)
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AuthControllerProtected {
    @PostMapping("/value")
    public String welcome(){
        return "welcome admin";
    }
}
