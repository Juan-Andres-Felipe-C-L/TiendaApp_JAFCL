package com.JuanAndresFelipe.TiendaApp.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    
    private String jwt;

    private UserDTO user;
}
