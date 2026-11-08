package com.JuanAndresFelipe.TiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JuanAndresFelipe.TiendaApp.entity.Products;
import java.util.List;


public interface ProductsRepository extends JpaRepository<Products, Long> {
    
    List<Products> findAllByCategoryId(Long categoryId);
}
