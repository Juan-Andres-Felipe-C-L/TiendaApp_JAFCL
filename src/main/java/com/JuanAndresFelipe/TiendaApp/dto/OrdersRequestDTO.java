package com.JuanAndresFelipe.TiendaApp.dto;

import lombok.Data;

@Data 
public class OrdersRequestDTO {
    
    private Long productId;

    private int quantity;
}
