package com.JuanAndresFelipe.TiendaApp.controller;

import org.springframework.web.bind.annotation.RestController;

import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.ProductsResponseDTO;
import com.JuanAndresFelipe.TiendaApp.service.ProductsService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductsController {
    
    private final ProductsService productsService;

    /**
     * Método público para poder observar el catálogo de productos registrados en la aplicación.
     * @param categoryId es un parámetro opcional para poder filtrar los productos por categorías.
     * @return un "ResponseEntity" con código Http, dentro del cual va una lista de productos en formato de DTO.
     */
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

    /**
     * Método público para ver sólo la información de un producto.
     * @param id es el I.D. registrado del producto que se quiere consultar.
     * @return un "ResponseEntity" con un código Http y la información del producto más un mensaje.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HttpGlobalResponseDTO<ProductsResponseDTO>> getProduct(@PathVariable Long id) {
        try {
            HttpGlobalResponseDTO<ProductsResponseDTO> response = productsService.getProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    
}
