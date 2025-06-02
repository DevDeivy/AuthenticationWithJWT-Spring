package com.example.JWTauth.JWTFilter;

import com.example.JWTauth.services.JWTservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Indicamos que esta clase es un componente de Spring para que se cargue automáticamente
@Component

// Lombok genera el constructor
@RequiredArgsConstructor

 // Heredamos de OncePerRequestFilter para ejecutar el filtro una vez por cada solicitud
public class JWTauthFilter extends OncePerRequestFilter {

    // Inyectamos los servicios necesarios para trabajar con JWT y obtener datos del usuario
    private final JWTservice jwtservice;
    private final UserDetailsService userDetailsService;

    // Este método se ejecuta automáticamente con cada solicitud HTTP
    // @Override indica que estamos sobrescribiendo un método de la clase padre
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException{

        // Obtenemos el token del encabezado de la solicitud
        final String token = getTokenFromRequest(request);
        final String username;

        // Si no hay token, dejamos que el filtro continúe sin hacer nada
        if(token == null){
            filterChain.doFilter(request, response);
            return;
        }

        // Obtenemos el nombre de usuario desde el token
        username = jwtservice.getUsernameFromToken(token);

        // Si se obtuvo el usuario y aún no está autenticado en el contexto de seguridad
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Cargamos los datos del usuario desde la base de datos (o sistema de autenticación configurado)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Verificamos que el token sea válido para ese usuario
            if (jwtservice.isTokenValid(token, userDetails)){
                // Creamos un token de autenticación con los datos del usuario y sus roles/permisos
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Asociamos detalles adicionales de la solicitud al token (como IP, sesión, etc.)
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establecemos el usuario autenticado en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continuamos con el siguiente filtro en la cadena (o el controlador, si ya no hay más filtros)
        filterChain.doFilter(request, response);
    }

    // Método privado para extraer el token del encabezado "Authorization"
    private String getTokenFromRequest(HttpServletRequest request) {
        // Obtenemos el header "Authorization" (ej: "Bearer eyJhbGciOiJIUz...")
        final String authHeader =  request.getHeader(HttpHeaders.AUTHORIZATION);

        // Verificamos que el header no esté vacío y que comience con "Bearer "
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            // Devolvemos solo el token (sin la palabra "Bearer ")
            return authHeader.substring(7);
        }
        // Si no cumple con las condiciones, retornamos null
        return null;
    }
}
