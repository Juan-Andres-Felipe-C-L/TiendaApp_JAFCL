package com.JuanAndresFelipe.TiendaApp.dto;

import lombok.Data;

@Data 
public class ClientRegisterRequestDTO {
    
    private String username;

    private String email;

    private String password;
}
