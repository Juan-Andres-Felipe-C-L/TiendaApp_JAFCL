package com.JuanAndresFelipe.TiendaApp.controller;

import org.springframework.web.bind.annotation.RestController;

import com.JuanAndresFelipe.TiendaApp.dto.CategoriesResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.ProductsResponseDTO;
import com.JuanAndresFelipe.TiendaApp.service.ProductsService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductsController {
    
    private final ProductsService productsService;

    @GetMapping()
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductsResponseDTO>>> getCatalogue(@RequestParam(required = false) Long categoryId) {
        try {
            HttpGlobalResponseDTO<List<ProductsResponseDTO>> response = productsService.getCatalogue(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
}
