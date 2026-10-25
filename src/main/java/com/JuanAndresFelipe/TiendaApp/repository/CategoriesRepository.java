package com.JuanAndresFelipe.TiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JuanAndresFelipe.TiendaApp.entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long>{
    
}
