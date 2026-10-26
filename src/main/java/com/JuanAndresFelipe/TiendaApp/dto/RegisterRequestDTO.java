package com.JuanAndresFelipe.TiendaApp.dto;

import com.JuanAndresFelipe.TiendaApp.enums.RoleEnum;

import lombok.Data;

@Data 
public class RegisterRequestDTO {
   
    private String username;

    private String email;

    private String password;

    private RoleEnum role;
}
