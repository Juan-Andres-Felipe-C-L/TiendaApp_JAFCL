package com.JuanAndresFelipe.TiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JuanAndresFelipe.TiendaApp.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    
}
