package com.example.orchidservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private String orchidId;
    private String orchidName;
    private String price;
    private String quantity;
    private String subtotal;
    private String orchidUrl;
}

