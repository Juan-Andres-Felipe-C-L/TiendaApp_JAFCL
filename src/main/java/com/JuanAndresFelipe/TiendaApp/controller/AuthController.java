package com.JuanAndresFelipe.TiendaApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JuanAndresFelipe.TiendaApp.dto.ClientRegisterRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.LoginRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.LoginResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.UserDTO;
import com.JuanAndresFelipe.TiendaApp.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor 
public class AuthController {

    private final AuthService authService;
   
    /**
     * Método para registrar un usuario en la aplicación como cliente.
     * @param request: Es un DTO que tiene la información necesaria del usuario para que este se pueda registrar.
     * @return un "ResponseEntity" que muestra un estatus Http, un mensaje y algunos datos del usuario que se registró.
     */
    @PostMapping("register")
    public ResponseEntity<HttpGlobalResponseDTO<UserDTO>> register(@RequestBody ClientRegisterRequestDTO request) {
        try {
            HttpGlobalResponseDTO<UserDTO> response = authService.register(request);
            
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
     * Método para hacer el Login de un usuario que está autenticado en la aplicación.
     * @param request: Es un DTO que tiene la información necesaria del usuario para que este se pueda hacer Login
     * @return un "ResponseEntity" que muestra un estatus Http, un mensaje y algunos datos del usuario que inició sesión en la aplicación.
     */
    @PostMapping("login")
    public ResponseEntity<HttpGlobalResponseDTO<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        try {
            HttpGlobalResponseDTO<LoginResponseDTO> response = authService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Método para que alguien autenticado, su JWT token sea renovado y pueda seguir actuando en la aplicación.
     * @param request: Es la intervención de una petición.
     * @return un "ResponseEntity" que muestra un estatus Http, un mensaje, algunos datos del usuario y un JWT token renovado.
     */
    @PostMapping("refreshtoken")
    public ResponseEntity<HttpGlobalResponseDTO<LoginResponseDTO>> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setMessage("Token invalido.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            response = authService.refreshToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Token invalido o expirado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
