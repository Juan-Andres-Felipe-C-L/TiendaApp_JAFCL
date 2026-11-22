package com.JuanAndresFelipe.TiendaApp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.ProductsRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.ProductsResponseDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Categories;
import com.JuanAndresFelipe.TiendaApp.entity.Products;
import com.JuanAndresFelipe.TiendaApp.entity.Users;
import com.JuanAndresFelipe.TiendaApp.repository.CategoriesRepository;
import com.JuanAndresFelipe.TiendaApp.repository.ProductsRepository;
import com.JuanAndresFelipe.TiendaApp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository productsRepository;

    private final CategoriesRepository categoriesRepository;

    private final JwtService jwtService;

    private final UsersRepository usersRepository;

    /**
     * Método para traer una lista de los productos registrados en el sistema.
     * También, estos pueden ser filtrados con el I.D. de la categoría de productos
     * que se quieren ver.
     * 
     * @param categoryId es un parámetro opcional para filtrar los productos por
     *                   categorías.
     * @return retorna una lista de DTOs para representar la información de los
     *         productos.
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
            response.setMessage("Estos son los productos registrados que pertenecen a la categoría: "
                    + categoryFound.get().getName() + ".");
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
     * 
     * @param Id es el parámetro del I.D. registrado del producto.
     * @return un DTO que expresa la información del producto consultado más un
     *         mensaje.
     */
    public HttpGlobalResponseDTO<ProductsResponseDTO> getProduct(Long Id) {
        HttpGlobalResponseDTO<ProductsResponseDTO> response = new HttpGlobalResponseDTO<>();

        Optional<Products> productFound = productsRepository.findById(Id);

        if (productFound.isEmpty()) {
            response.setMessage("No existe un producto registrado con ese I.D. en el sistema.");
            return response;
        }

        ProductsResponseDTO productDTO = new ProductsResponseDTO();

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

    /**
     * Método para que los vendedores puedan consultar sus productos en la
     * aplicación.
     * 
     * @param token JWT es un String del cual se saca la información del I.D. del
     *              vendedor, que se utiliza como parámetro para hacer la consulta.
     * @return un DTO global, con la lista de prodcutos pertenecientes al vendedor y
     *         un mensaje.
     */
    public HttpGlobalResponseDTO<List<ProductsResponseDTO>> getMyProducts(String token) {
        HttpGlobalResponseDTO<List<ProductsResponseDTO>> response = new HttpGlobalResponseDTO<>();

        Long sellerId = jwtService.extractUserId(token);

        List<Products> productsFound = productsRepository.findAllBySellerId(sellerId);

        if (productsFound.isEmpty()) {
            response.setMessage("Usuario, usted no ha publicado ningún producto en esta aplicacicón.");
            return response;
        }

        List<ProductsResponseDTO> listDTO = new ArrayList<>();

        for (Products products : productsFound) {
            ProductsResponseDTO productsDTO = new ProductsResponseDTO();
            productsDTO.setId(products.getId());
            productsDTO.setName(products.getName());
            productsDTO.setDescription(products.getDescription());
            productsDTO.setPrice(products.getPrice());
            productsDTO.setStock(products.getStock());
            productsDTO.setCategoryId(products.getCategory().getId());
            productsDTO.setCategoryName(products.getCategory().getName());
            productsDTO.setSellerId(products.getSeller().getId());
            productsDTO.setSellerName(products.getSeller().getUsername());

            listDTO.add(productsDTO);
        }

        response.setData(listDTO);
        response.setMessage("Vendedor, estos son sus productos.");

        return response;
    }

    /**
     * Método para registar un producto en el sistema.
     * 
     * @param token    es un String, del cual, se planea extraer la información del
     *                 I.D. del vendedor, para ser utilizado en el proceso de la
     *                 función.
     * @param registry es un DTO por medio del cual, se obtiene la información del
     *                 producto a registrar.
     * @return un DTO que aporta la información del producto registrado y mensaje
     *         acerca del estado de la petición.
     */
    public HttpGlobalResponseDTO<ProductsResponseDTO> registerProduct(String token, ProductsRequestDTO registry) {
        HttpGlobalResponseDTO<ProductsResponseDTO> response = new HttpGlobalResponseDTO<>();

        if (registry.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            response.setMessage(
                    "Usuario, no se le puede poner un precio menor o igual que 0 a un producto.");
            return response;
        }

        if (registry.getStock() <= 0) {
            response.setMessage(
                    "Usuario, no se le puede poner un stock menor o igual que 0 a un producto.");
            return response;
        }

        Optional<Categories> categoryFound = categoriesRepository.findById(registry.getCategoryId());

        if (categoryFound.isEmpty()) {
            response.setMessage(
                    "El I.D. de categoría escrito para el producto no se encuentra registrada dentro del sistema.");
            return response;
        }

        Long sellerId = jwtService.extractUserId(token);

        Optional<Users> sellerFound = usersRepository.findById(sellerId);

        Optional<Products> productFound = productsRepository.findByName(registry.getName());

        if (productFound.isPresent()) {

            if (productFound.get().getName().equalsIgnoreCase(registry.getName())
                    && productFound.get().getSeller().getId() == sellerId) {
                response.setMessage("Usuario, usted ya ha registrado ese producto anteriormente.");
                return response;
            }
        }

        ProductsResponseDTO productDTO = new ProductsResponseDTO();
        Products product = new Products();

        product.setName(registry.getName());
        product.setDescription(registry.getDescription());
        product.setPrice(registry.getPrice());
        product.setStock(registry.getStock());
        product.setCategory(categoryFound.get());
        product.setSeller(sellerFound.get());

        productsRepository.save(product);

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setSellerId(product.getSeller().getId());
        productDTO.setSellerName(product.getSeller().getUsername());

        response.setData(productDTO);
        response.setMessage("El producto ha sido registrado con éxito.");
        return response;
    }

    /**
     * Método para poder actualizar la información de un producto. Lo que no se puede cambiar son la información propia de los vendedores.
     * @param id es el I.D. del prodcuto próximo a actualizar.
     * @param registry es el DTO que se necesita para obtener la nueva información.
     * @param token JWT que se utiliza para obtener el I.D. del usuario que está realizando la petición.
     * @return un DTO general que trae un mensaje y la información del producto actualizado.
     */
    public HttpGlobalResponseDTO<ProductsResponseDTO> editProduct(Long id, ProductsRequestDTO registry, String token) {
        HttpGlobalResponseDTO<ProductsResponseDTO> response = new HttpGlobalResponseDTO<>();

        if (registry.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            response.setMessage(
                    "Usuario, no se le puede poner un precio menor o igual que 0 a un producto.");
            return response;
        }

        if (registry.getStock() <= 0) {
            response.setMessage(
                    "Usuario, no se le puede poner un stock menor o igual que 0 a un producto.");
            return response;
        }
        
        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            response.setMessage("El I.D. escrito no lo tiene ningún producto en el sistema.");
            return response;
        }

        Optional<Categories> categoryFound = categoriesRepository.findById(registry.getCategoryId());

        if (categoryFound.isEmpty()) {
            response.setMessage("El I.D. de categoría escrito no existe en el sistema.");
            return response;
        }

        Long sellerId = jwtService.extractUserId(token);

        Optional<Users> userFound = usersRepository.findById(sellerId);

        ProductsResponseDTO productDTO = new ProductsResponseDTO();
        Products product = productFound.get();

        if (userFound.get().getRole().getId() == 3) {

            if (productFound.get().getSeller().getId() == userFound.get().getId()) {
                
                product.setName(registry.getName());
                product.setDescription(registry.getDescription());
                product.setPrice(registry.getPrice());
                product.setStock(registry.getStock());
                product.setCategory(categoryFound.get());
                product.setSeller(userFound.get());

                productsRepository.save(product);

                productDTO.setId(product.getId());
                productDTO.setName(product.getName());
                productDTO.setDescription(product.getDescription());
                productDTO.setPrice(product.getPrice());
                productDTO.setStock(product.getStock());
                productDTO.setCategoryId(product.getCategory().getId());
                productDTO.setCategoryName(product.getCategory().getName());
                productDTO.setSellerId(product.getSeller().getId());
                productDTO.setSellerName(product.getSeller().getUsername());

                response.setData(productDTO);
                response.setMessage("El producto ha sido actualizado con éxito.");
            
            } else {
                
                response.setMessage("Usuario, usted no puede actualizar productos que no sean suyos.");
                return response;
            }

        } else if (userFound.get().getRole().getId() == 1) {

            product.setName(registry.getName());
            product.setDescription(registry.getDescription());
            product.setPrice(registry.getPrice());
            product.setStock(registry.getStock());
            product.setCategory(categoryFound.get());

            productsRepository.save(product);

            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setCategoryId(product.getCategory().getId());
            productDTO.setCategoryName(product.getCategory().getName());
            productDTO.setSellerId(product.getSeller().getId());
            productDTO.setSellerName(product.getSeller().getUsername());

            response.setData(productDTO);
            response.setMessage("El producto ha sido actualizado con éxito.");
        }

        return response;
    }





    public HttpGlobalResponseDTO<ProductsResponseDTO> deleteProduct(Long id, String token) {
        HttpGlobalResponseDTO<ProductsResponseDTO> response = new HttpGlobalResponseDTO<>();

        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            response.setMessage("El I.D. escrito no lo tiene ningún producto en el sistema.");
            return response;
        }

        Long sellerId = jwtService.extractUserId(token);

        Optional<Users> userFound = usersRepository.findById(sellerId);

        ProductsResponseDTO productDTO = new ProductsResponseDTO();

        if (userFound.get().getRole().getId() == 3) {

            if (productFound.get().getSeller().getId() == userFound.get().getId()) {
                
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

                productsRepository.deleteById(productFound.get().getId());                

                response.setMessage("El producto ha sido eliminado con éxito.");
           
            } else {
                
                response.setMessage("Usuario, usted no puede eliminar productos que no sean suyos.");
                return response;
            }

        } else if (userFound.get().getRole().getId() == 1) {

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
            
            productsRepository.deleteById(productFound.get().getId());

            response.setMessage("El producto ha sido eliminado con éxito.");
        }

        return response;
    }
}
