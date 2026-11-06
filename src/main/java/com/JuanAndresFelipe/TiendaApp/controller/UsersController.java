package com.JuanAndresFelipe.TiendaApp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.UserDTO;
import com.JuanAndresFelipe.TiendaApp.enums.RoleEnum;
import com.JuanAndresFelipe.TiendaApp.security.RequiresRole;
import com.JuanAndresFelipe.TiendaApp.service.UsersService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    
    private final UsersService usersService;

    /**
     * Método para que los administradores obtengan una lista de los usuarios que hay registrados en la aplicacion.
     * @return un "ResponseEntity" con un código HTTP y una lista de usuarios con algunos datos de su información.
     */
    @GetMapping()
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<List<UserDTO>>> getUsers() {
        try {
            HttpGlobalResponseDTO<List<UserDTO>> response = usersService.getUsers();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
