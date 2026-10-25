package com.JuanAndresFelipe.TiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JuanAndresFelipe.TiendaApp.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    
}
