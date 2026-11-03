package com.JuanAndresFelipe.TiendaApp.security;

import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class RoleInterceptor implements HandlerInterceptor {
    
    /**
     * Método para interceptar las peticiones del cliente a la aplicación. El método está sobreescrito para acomodarlo a cómo nosotros queramos.
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod method)) {
            return true; 
        }

        RequiresRole annotation = method.getMethodAnnotation(RequiresRole.class);

        if (annotation == null) {
            return true;
        }

        Object rol = request.getAttribute("rolId");

        if (!(rol instanceof Long rolId)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"Error\": \"Usuario no autenticado.\"}");
            return false; // mochamos la petición, no alcanza a llegar al controller
        }

        Boolean hasRole = Arrays.stream(annotation.value()).anyMatch(role -> role.getId() == rolId);

        if (!hasRole) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"Error\": \"Usuario, no tiene permiso para ejecutar esta acción. \"}");
            return false;
        }

        return true;
    }
}
