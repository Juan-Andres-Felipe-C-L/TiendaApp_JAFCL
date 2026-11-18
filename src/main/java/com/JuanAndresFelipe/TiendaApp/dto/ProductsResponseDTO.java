package com.JuanAndresFelipe.TiendaApp.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data 
public class ProductsResponseDTO {

    private Long id;
    
    private String name;

    private String description;

    private BigDecimal price;

    private int stock;

    private Long categoryId;

    private String categoryName;

    private Long sellerId;

    private String sellerName;
}
