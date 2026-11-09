package com.JuanAndresFelipe.TiendaApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.dto.CategoriesResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.ProductsResponseDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Categories;
import com.JuanAndresFelipe.TiendaApp.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ProductsService {
    
    private final ProductsRepository productsRepository;

    public HttpGlobalResponseDTO<List<ProductsResponseDTO>> getCatalogue(Long categoryId) {
        HttpGlobalResponseDTO<List<ProductsResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<ProductsResponseDTO> listDTO = new ArrayList<>();

        if (categoryId != null) {
            
            List
        }

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
