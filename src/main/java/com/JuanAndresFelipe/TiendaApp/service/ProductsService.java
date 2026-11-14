package com.JuanAndresFelipe.TiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.ProductsResponseDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Categories;
import com.JuanAndresFelipe.TiendaApp.entity.Products;
import com.JuanAndresFelipe.TiendaApp.repository.CategoriesRepository;
import com.JuanAndresFelipe.TiendaApp.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ProductsService {
    
    private final ProductsRepository productsRepository;

    private final CategoriesRepository categoriesRepository;

    /**
     * Método para traer una lista de los productos registrados en el sistema. También, estos pueden ser filtrados con el I.D. de la categoría de productos que se quieren ver.
     * @param categoryId es un parámetro opcional para filtrar los productos por categorías.
     * @return retorna una lista de DTOs para representar la información de los productos.
     */
    public HttpGlobalResponseDTO<List<ProductsResponseDTO>> getCatalogue(Long categoryId) {
        HttpGlobalResponseDTO<List<ProductsResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<ProductsResponseDTO> listDTO = new ArrayList<>();

        if (categoryId != null) {
            
            Optional<Categories> categoryFound = categoriesRepository.findById(categoryId);

            if (categoryFound.isEmpty()) {
                response.setMessage("El I.D. de categoría escrito no se encuentra registrado en el sistema.");
                return response;
            }

            List<Products> productsFound = productsRepository.findAllByCategoryId(categoryFound.get().getId());

            for (Products product : productsFound) {
                ProductsResponseDTO productsDTO = new ProductsResponseDTO();
                productsDTO.setId(product.getId());
                productsDTO.setName(product.getName());
                productsDTO.setDescription(product.getDescription());
                productsDTO.setPrice(product.getPrice());
                productsDTO.setStock(product.getStock());
                productsDTO.setCategoryId(product.getCategory().getId());
                productsDTO.setCategoryName(product.getCategory().getName());
                productsDTO.setSellerId(product.getSeller().getId());
                productsDTO.setSellerName(product.getSeller().getUsername());
                
                listDTO.add(productsDTO);
            }

            response.setData(listDTO);
            response.setMessage("Estos son los productos registrados que pertenecen a la categoría: " + categoryFound.get().getName() + ".");
            return response;
        }

        List<Products> productsFound = productsRepository.findAll();

            for (Products product : productsFound) {
                ProductsResponseDTO productsDTO = new ProductsResponseDTO();
                productsDTO.setId(product.getId());
                productsDTO.setName(product.getName());
                productsDTO.setDescription(product.getDescription());
                productsDTO.setPrice(product.getPrice());
                productsDTO.setStock(product.getStock());
                productsDTO.setCategoryId(product.getCategory().getId());
                productsDTO.setCategoryName(product.getCategory().getName());
                productsDTO.setSellerId(product.getSeller().getId());
                productsDTO.setSellerName(product.getSeller().getUsername());
                
                listDTO.add(productsDTO);
            }

        response.setData(listDTO);
        response.setMessage("Estos son los productos que conforman el catálogo del sistema.");

        return response;
    }
    
    /**
     * Método para consultar la información de un producto.
     * @param Id es el parámetro del I.D. registrado del producto.
     * @return un DTO que expresa la información del producto consultado más un mensaje.
     */
    public HttpGlobalResponseDTO<ProductsResponseDTO> getProduct(Long Id) {
        HttpGlobalResponseDTO<ProductsResponseDTO> response = new HttpGlobalResponseDTO<>();
        ProductsResponseDTO productDTO = new ProductsResponseDTO();

        Optional<Products> productFound = productsRepository.findById(Id);

        if (productFound.isEmpty()) {
            response.setMessage("No existe un producto registrado con ese I.D. en el sistema.");
            return response;
        }

        productDTO.setId(productFound.get().getId());
        productDTO.setName(productFound.get().getName());
        productDTO.setDescription(productFound.get().getDescription());
        productDTO.setPrice(productFound.get().getPrice());
        productDTO.setStock(productFound.get().getStock());        
        productDTO.setCategoryId(productFound.get().getCategory().getId());
        productDTO.setCategoryName(productFound.get().getCategory().getName());
        productDTO.setSellerId(productFound.get().getSeller().getId());
        productDTO.setSellerName(productFound.get().getSeller().getUsername());

        response.setData(productDTO);
        response.setMessage("Producto obtenido al consultar el I.D. número: " + Id + ".");
        return response;
    }
}
