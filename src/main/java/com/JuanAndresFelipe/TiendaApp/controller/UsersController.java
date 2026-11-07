package com.JuanAndresFelipe.TiendaApp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JuanAndresFelipe.TiendaApp.dto.ChangeRoleDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.RegisterRequestDTO;
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

    /**
     * Método para que un administrador pueda registrar un usuario en la aplicación, otorgandole cualquier tipo de rol.
     * @param request: Es un DTO que tiene la información necesaria del usuario para que este pueda ser registrado por un administrador.
     * @return un "ResponseEntity" que muestra un estatus Http, un mensaje y algunos datos del usuario que se registró.
     */
    @PostMapping()
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<UserDTO>> register(@RequestBody RegisterRequestDTO request) {
        try {
            HttpGlobalResponseDTO<UserDTO> response = usersService.register(request);
            
            if (response.getData() == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Método para que un administrador pueda cambiar el rol de un usuario registrado.
     * @param id es el I.D. del usuario a quien se le quiere hacer la actualización.
     * @param request es un DTO utilizado para ingresar el I.D. del nuevo rol del usuario a quien se le quiere hacer la edición.
     * @return un "ResponseEntity" con un Http status, un mensaje y la información del usuario y su nuevo rol.
     */
    @PatchMapping("/{id}/role")
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<UserDTO>> changeId(@PathVariable Long id, @RequestBody ChangeRoleDTO request) {
        try {
            HttpGlobalResponseDTO<UserDTO> response = usersService.changeId(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
