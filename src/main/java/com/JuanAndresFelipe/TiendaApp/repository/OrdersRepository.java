package com.JuanAndresFelipe.TiendaApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JuanAndresFelipe.TiendaApp.entity.Orders;


public interface OrdersRepository extends JpaRepository<Orders, Long>{
    
    List<Orders> findAllByClientId(Long clientId);

    List<Orders> findAllByProductSellerId(Long sellerId);
}
