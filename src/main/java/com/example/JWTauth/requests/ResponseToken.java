package com.example.JWTauth.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//esta clase tiene como funcionalidad devolver el token al usuario
public class ResponseToken {
    String token;
}
