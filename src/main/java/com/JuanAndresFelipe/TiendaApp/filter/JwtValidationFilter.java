package com.JuanAndresFelipe.TiendaApp.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.JuanAndresFelipe.TiendaApp.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
     
    /**
     * Método que coordina cómo aplicar el filtro interno para hallar un token jwt.
     * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse, jakarta.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"Error\": \"El encabezado de autorización no está presente.\"}");
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            if (jwtService.isTokenValid(token)) {

                String username = jwtService.extractUsername(token);
                Long userId = jwtService.extractUserId(token);
                Long rolId = jwtService.extractRolId(token);

                request.setAttribute("username", username);
                request.setAttribute("userId", userId);
                request.setAttribute("rolId", rolId);

                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"Error\": \"El token está inavlidado o expiró.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"Error\": \"Falló la validación.\"}");
        }
    }

    /**
     * Método que indica a cuáles rutas no se les debe de aplicar el filtro interno para validar si el usuario tiene token jwt.
     * @see org.springframework.web.filter.OncePerRequestFilter#shouldNotFilter(jakarta.servlet.http.HttpServletRequest)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.startsWith("/api/v1/tienda/auth/")) {
            return true;
        }

        if (method.equals("GET") &&
                (path.equals("/api/v1/tienda/products") ||
                path.matches("/api/v1/tienda/products/\\d+"))) {
            return true;
        }

        return false;
    }
}
