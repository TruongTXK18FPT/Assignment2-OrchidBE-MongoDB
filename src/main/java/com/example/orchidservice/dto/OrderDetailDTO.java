package com.example.orchidservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private String id;
    private String orchidId;
    private String orchidName; // For display purposes
    private String orchidUrl; // For display purposes
    private String price;
    private String quantity;
    private String orderId;
    private String subtotal; // Calculated field (price * quantity)
}
