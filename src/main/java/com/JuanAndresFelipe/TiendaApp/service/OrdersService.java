package com.JuanAndresFelipe.TiendaApp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.OrdersRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.OrdersResponseDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Orders;
import com.JuanAndresFelipe.TiendaApp.entity.Products;
import com.JuanAndresFelipe.TiendaApp.entity.Users;
import com.JuanAndresFelipe.TiendaApp.repository.OrdersRepository;
import com.JuanAndresFelipe.TiendaApp.repository.ProductsRepository;
import com.JuanAndresFelipe.TiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {
    
    private final OrdersRepository ordersRepository;

    private final ProductsRepository productsRepository;

    private final JwtService jwtService;

    private final UsersRepository usersRepository;

    /**
     * Método que permite registrar un pedido.
     * @param registry es el DTO con el que se pasa la información del pedido.
     * @param token JWT que se utiliza para extraer la información de quien está haciendo el regsitro del pedido.
     * @return un DTO general que muestra la infomración del pedido y un mensaje de su estado.
     */
    public HttpGlobalResponseDTO<OrdersResponseDTO> registerOrder(OrdersRequestDTO registry, String token) {
        HttpGlobalResponseDTO<OrdersResponseDTO> response = new HttpGlobalResponseDTO<>();

        Optional<Products> productFound = productsRepository.findById(registry.getProductId());

        if (productFound.isEmpty()) {
            response.setMessage("El I.D. escrito no lo tiene ningún producto en el sistema.");
            return response;
        }

        if (registry.getQuantity() <= 0) {
            response.setMessage("Usuario, no se pueden hacer pedidos de productos menores o iguales a 0 unidades.");
            return response;
        }

        if ((productFound.get().getStock() - registry.getQuantity()) < 0) {
            response.setMessage("El stock actual no alcanza a proporcionar los " + registry.getQuantity() + " productos.");
            return response;
        }

        Long clientId = jwtService.extractUserId(token);
        Optional<Users> userFound = usersRepository.findById(clientId);

        int newQuantity = productFound.get().getStock() - registry.getQuantity();

        BigDecimal quantityBigDecimal = BigDecimal.valueOf(registry.getQuantity());
        
        BigDecimal total =  quantityBigDecimal.multiply(productFound.get().getPrice());


        
        Products product = productFound.get();

        product.setStock(newQuantity);

        productsRepository.save(product);
        
        
        
        Orders order = new Orders();

        order.setClient(userFound.get());
        order.setProduct(productFound.get());
        order.setQuantity(registry.getQuantity());
        order.setTotal(total);

        ordersRepository.save(order);

        OrdersResponseDTO orderDTO = new OrdersResponseDTO();

        orderDTO.setId(order.getId());
        orderDTO.setClientId(order.getClient().getId());
        orderDTO.setUsername(order.getClient().getUsername());
        orderDTO.setProductId(order.getProduct().getId());
        orderDTO.setProductName(order.getProduct().getName());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCreatedAt(order.getCreatedAt());

        response.setData(orderDTO);
        response.setMessage("Pedido registrado exitosamente.");
        return response;        
    }

    /**
     * Método que permite ver todos los pedidos de un usuario tipo cliente.
     * @param token es de donde se extrae la información del usuario que está realizando la petición.
     * @return un DTO general que trae un mensaje y una lista de todos los pedidos del usuario.
     */
    public HttpGlobalResponseDTO<List<OrdersResponseDTO>> getMyOrders(String token) {
        HttpGlobalResponseDTO<List<OrdersResponseDTO>> response = new HttpGlobalResponseDTO<>();

        Long clientId = jwtService.extractUserId(token);

        List<Orders> ordersFound = ordersRepository.findAllByClientId(clientId);

        if (ordersFound.isEmpty()) {
            response.setMessage("Usuario, usted no ha realizado ningún pedido en esta aplicación.");
            return response;
        }

        List<OrdersResponseDTO> listDTO = new ArrayList<>();

        for (Orders order : ordersFound) {
            OrdersResponseDTO ordersDTO = new OrdersResponseDTO();
            ordersDTO.setId(order.getId());
            ordersDTO.setClientId(order.getClient().getId());
            ordersDTO.setUsername(order.getClient().getUsername());
            ordersDTO.setProductId(order.getProduct().getId());
            ordersDTO.setProductName(order.getProduct().getName());
            ordersDTO.setQuantity(order.getQuantity());
            ordersDTO.setTotal(order.getTotal());
            ordersDTO.setStatus(order.getStatus());
            ordersDTO.setCreatedAt(order.getCreatedAt());

            listDTO.add(ordersDTO);
        }

        response.setData(listDTO);
        response.setMessage("Cliente, estos son sus pedidos.");

        return response;
    }





    /**///////////////VENDEDORES Obtener pedidos de sus productos. */



    
    
    /**
     * Método para ver los pedidos realizados en la aplicación.
     * @return un DTO que trae toda la información de los pedidos de la aplicación y un mensaje.
     */
    public HttpGlobalResponseDTO<List<OrdersResponseDTO>> getOrders() {
        HttpGlobalResponseDTO<List<OrdersResponseDTO>> response = new HttpGlobalResponseDTO<>();
        
        List<Orders> ordersFound = ordersRepository.findAll();

        if (ordersFound.isEmpty()) {
            response.setMessage("Administrador, aún no hay pedidos registrados en el sistema.");
            return response;
        }

        List<OrdersResponseDTO> listOrders = new ArrayList<>();

        for (Orders order : ordersFound) {
            OrdersResponseDTO orderDTO = new OrdersResponseDTO();
            orderDTO.setId(order.getId());
            orderDTO.setClientId(order.getClient().getId());
            orderDTO.setUsername(order.getClient().getUsername());
            orderDTO.setProductId(order.getProduct().getId());
            orderDTO.setProductName(order.getProduct().getName());
            orderDTO.setQuantity(order.getQuantity());
            orderDTO.setTotal(order.getTotal());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setCreatedAt(order.getCreatedAt());

            listOrders.add(orderDTO);
        }

        response.setMessage("Estos son los pedidos encontrados.");
        response.setData(listOrders);

        return response;
    }
}
