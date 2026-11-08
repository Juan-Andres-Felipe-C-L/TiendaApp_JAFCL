package com.JuanAndresFelipe.TiendaApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JuanAndresFelipe.TiendaApp.entity.Categories;


public interface CategoriesRepository extends JpaRepository<Categories, Long>{
    
    Optional<Categories> findByName(String name);
}
