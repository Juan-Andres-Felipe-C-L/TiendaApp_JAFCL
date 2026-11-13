package com.JuanAndresFelipe.TiendaApp.dto;

import lombok.Data;

@Data 
public class ProductsResponseDTO {

    private Long id;
    
    private String name;

    private String description;

    private double price;

    private int stock;

    private Long categoryId;

    private String categoryName;

    private Long sellerId;

    private String sellerName;
}
