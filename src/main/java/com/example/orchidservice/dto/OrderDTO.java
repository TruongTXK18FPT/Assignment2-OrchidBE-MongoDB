package com.example.orchidservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String id;
    private String accountId;
    private String accountName; // For display purposes
    private LocalDate orderDate;
    private String orderStatus;
    private String totalAmount;
    private List<OrderDetailDTO> orderDetails;
}
