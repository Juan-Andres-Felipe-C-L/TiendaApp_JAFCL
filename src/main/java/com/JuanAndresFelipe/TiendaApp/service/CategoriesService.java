package com.JuanAndresFelipe.TiendaApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.dto.CategoriesResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Categories;
import com.JuanAndresFelipe.TiendaApp.repository.CategoriesRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CategoriesService {
    
    private final CategoriesRepository categoriesRepository;

    /**
     * Método que muestra todas las categorías de productos que hay en la aplicación.
     * @return un DTO compuesto por la lista de categorías de productos y un mensaje.
     */
    public HttpGlobalResponseDTO<List<CategoriesResponseDTO>> getCategories() {
        HttpGlobalResponseDTO<List<CategoriesResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<CategoriesResponseDTO> listDTO = new ArrayList<>();

        List<Categories> usersFound = categoriesRepository.findAll();

        for (Categories categories : usersFound) {
            CategoriesResponseDTO categoriesDTO = new CategoriesResponseDTO();
            categoriesDTO.setId(categories.getId());
            categoriesDTO.setName(categories.getName());

            listDTO.add(categoriesDTO);
        }

        response.setData(listDTO);
        response.setMessage("Estas son la categorias registradas hasta ahora.");

        return response;
    }
}
