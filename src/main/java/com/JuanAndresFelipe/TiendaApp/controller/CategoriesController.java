package com.JuanAndresFelipe.TiendaApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;
    
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
    
}
