package com.JuanAndresFelipe.TiendaApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.OrdersRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.OrdersResponseDTO;
import com.JuanAndresFelipe.TiendaApp.enums.RoleEnum;
import com.JuanAndresFelipe.TiendaApp.security.RequiresRole;
import com.JuanAndresFelipe.TiendaApp.service.OrdersService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {
    
    private final OrdersService ordersService;

    /**
     * Método para que un usaurio, del tipo cliente, pueda registrar un pedido.
     * @param registry es el DTO para pasar la información del pedido.
     * @param request es la información de la petición para poder extraer el JWT token.
     * @return un "ResponseEntity" que trae un código Http, la información del pedido y un mensaje.
     */
    @PostMapping()
    @RequiresRole(RoleEnum.CLIENTE)
    public ResponseEntity<HttpGlobalResponseDTO<OrdersResponseDTO>> registerOrder(@RequestBody OrdersRequestDTO registry, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrdersResponseDTO> response = new HttpGlobalResponseDTO<>();
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setMessage("Token invalido.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.replace("Bearer ", "");
        
        try {
            response = ordersService.registerOrder(registry, token);
            if (response.getMessage().equals("El I.D. escrito no lo tiene ningún producto en el sistema.") || response.getMessage().equals("Usuario, no se pueden hacer pedidos de productos menores o iguales a 0 unidades.")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else if (response.getData() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Método para que un cliente pueda ver la información de todos sus pedidos.
     * @param request es la petición que se utiliza para extraer el JWT token.
     * @return un "ResponseEntity" con un código Http, una lista de todos los pedidos del cliente y un mensaje.
     */
    @GetMapping("/mine")
    @RequiresRole(RoleEnum.CLIENTE)
    public ResponseEntity<HttpGlobalResponseDTO<List<OrdersResponseDTO>>> getMyOrders(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrdersResponseDTO>> response = new HttpGlobalResponseDTO<>();
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setMessage("Token invalido.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.replace("Bearer ", "");
        
        try {
            response = ordersService.getMyOrders(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }




    /**///////////////VENDEDORES Obtener pedidos de sus productos. */



    /**
     * Método para que los usuarios, del tipo administrador, puedan ver todos los pedidos que se han hecho en la aplicación.
     * @return un "ResponseEntity" con un DTO que trae toda la infromación de los pedidos, un código Http y un mensaje.
     */
    @GetMapping()
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<List<OrdersResponseDTO>>> getOrders() {        
        try {
            HttpGlobalResponseDTO<List<OrdersResponseDTO>> response = ordersService.getOrders();
            return ResponseEntity.status(HttpStatus.OK).body(response);            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
}
