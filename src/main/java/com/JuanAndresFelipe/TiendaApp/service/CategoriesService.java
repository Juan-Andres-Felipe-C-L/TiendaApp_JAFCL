package com.JuanAndresFelipe.TiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.dto.CategoriesRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.CategoriesResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Categories;
import com.JuanAndresFelipe.TiendaApp.entity.Products;
import com.JuanAndresFelipe.TiendaApp.repository.CategoriesRepository;
import com.JuanAndresFelipe.TiendaApp.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CategoriesService {
    
    private final CategoriesRepository categoriesRepository;

    private final ProductsRepository productsRepository;

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

    /**
     * Método para registar una nueva categoría en el sistema.
     * @param request es un DTO para brindar la información de la nueva categoría.
     * @return un DTO global con la información de la nueva categoría registrada y un mensaje.
     */
    public HttpGlobalResponseDTO<CategoriesResponseDTO> registerCategory(CategoriesRequestDTO request) {
        HttpGlobalResponseDTO<CategoriesResponseDTO> response = new HttpGlobalResponseDTO<>();
        CategoriesResponseDTO categoryDTO = new CategoriesResponseDTO();

        Optional<Categories> categoryFound = categoriesRepository.findByName(request.getName());

        if (categoryFound.isPresent()) {
            response.setMessage("Ya hay una categoría de productos registrada con ese nombre.");
            return response;
        }

        Categories category = new Categories();
        category.setName(request.getName());

        categoriesRepository.save(category);

        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        response.setData(categoryDTO);
        response.setMessage("La categoría ha sido registrada exitosamente.");

        return response;
    }

    /**
     * Método para poder remover una categoría de productos del sistema, si no tiene productos.
     * @param id es el I.D. de la categoría que se quiere remover.
     * @return un DTO que brinda la información de la categoría y un mensaje para confirmar se ella fue borrada con éxito.
     */
    public HttpGlobalResponseDTO<CategoriesResponseDTO> deleteCategory(Long id) {
        HttpGlobalResponseDTO<CategoriesResponseDTO> response = new HttpGlobalResponseDTO<>();
        CategoriesResponseDTO categoryDTO = new CategoriesResponseDTO();

        Optional<Categories> categoryFound = categoriesRepository.findById(id);

        if (categoryFound.isEmpty()) {
            response.setMessage("El I.D. de categoría escrito no existe en el sistema.");
            return response;
        }

        List<Products> productsFound = productsRepository.findAllByCategoryId(id);

        if (productsFound.size() > 0) {
            response.setMessage("Esta categoría de productos no se puede eliminar porque contiene productos en ella.");
            return response;
        }

        categoryDTO.setId(categoryFound.get().getId());
        categoryDTO.setName(categoryFound.get().getName());
        response.setData(categoryDTO);

        categoriesRepository.deleteById(id);
        
        response.setMessage("La categoría ha sido removida exitosamente.");

        return response;
    }
}
