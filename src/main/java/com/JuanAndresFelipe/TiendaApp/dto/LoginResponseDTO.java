package com.JuanAndresFelipe.TiendaApp.dto;

import com.JuanAndresFelipe.TiendaApp.entity.Users;

import lombok.Data;

@Data
public class LoginResponseDTO {
    
    private String jwt;

    private Users user;
}
