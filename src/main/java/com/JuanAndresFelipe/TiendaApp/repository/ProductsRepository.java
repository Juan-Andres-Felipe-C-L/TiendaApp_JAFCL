package com.JuanAndresFelipe.TiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JuanAndresFelipe.TiendaApp.entity.Products;
import java.util.List;
import java.util.Optional;


public interface ProductsRepository extends JpaRepository<Products, Long> {
    
    List<Products> findAllByCategoryId(Long categoryId);

    List<Products> findAllBySellerId(Long sellerId);

    Optional<Products> findByName(String name);
}
