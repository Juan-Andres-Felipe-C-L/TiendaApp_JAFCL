package com.JuanAndresFelipe.TiendaApp.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data 
public class ProductsRequestDTO {
    
    private String name;

    private String description;

    private BigDecimal price;

    private int stock;

    private Long categoryId;
}
