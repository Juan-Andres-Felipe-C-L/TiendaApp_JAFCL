package com.JuanAndresFelipe.TiendaApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JuanAndresFelipe.TiendaApp.dto.CategoriesRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.CategoriesResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.UserDTO;
import com.JuanAndresFelipe.TiendaApp.enums.RoleEnum;
import com.JuanAndresFelipe.TiendaApp.security.RequiresRole;
import com.JuanAndresFelipe.TiendaApp.service.CategoriesService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;
    
    /**
     * Método que pueden usar todos los usuarios de la aplicación para averiguar cuáles son las categorías de productos que se venden en este sistema.  
     * @return un "ResponseEntity" que indica un çódigo Http y muestra una lista de las categorías con su información, y además, un mensaje.
     */
    @GetMapping()
    @RequiresRole({RoleEnum.ADMIN, RoleEnum.VENDEDOR, RoleEnum.CLIENTE})
    public ResponseEntity<HttpGlobalResponseDTO<List<CategoriesResponseDTO>>> getCategories() {
        try {
            HttpGlobalResponseDTO<List<CategoriesResponseDTO>> response = categoriesService.getCategories();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    /**
     * Función para que los usuarios con rol de administrador puedan registrar una nueva categoría de productos en el sistema.
     * @param request es un DTO utilizado para brindar la información de la nueva categoría a registrar.
     * @return un "ResponseEntity" con un código Http, la información de la categoría registrada y un mensaje.
     */
    @PostMapping()
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<CategoriesResponseDTO>> registerCategory(@RequestBody CategoriesRequestDTO request) {        
        try {
            HttpGlobalResponseDTO<CategoriesResponseDTO> response = categoriesService.registerCategory(request);
            
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
    
}
