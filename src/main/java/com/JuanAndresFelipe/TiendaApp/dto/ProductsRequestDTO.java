package com.JuanAndresFelipe.TiendaApp.dto;

import lombok.Data;

@Data 
public class ProductsRequestDTO {
    
    private String name;

    private String description;

    private double price;

    private int stock;

    private int categoryId;

    private int sellerId;
}
