package com.example.orchidservice.service.imp;

import com.example.orchidservice.dto.OrderDTO;
import com.example.orchidservice.dto.OrderDetailDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderDTO> getAllOrders();
    Optional<OrderDTO> getOrderById(String id);
    OrderDTO saveOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(String id, OrderDTO orderDTO);
    void deleteOrder(String id);
    List<OrderDTO> getOrdersByAccount(String accountId);
    List<OrderDTO> getOrdersByStatus(String status);
    List<OrderDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate);
    OrderDTO updateOrderStatus(String id, String status);
    String calculateOrderTotal(String orderId);
    // Get order details for a specific order
    List<OrderDetailDTO> getOrderDetailsByOrderId(String orderId);
    // Get a specific order detail by ID
    Optional<OrderDetailDTO> getOrderDetailById(String orderDetailId);
    OrderDTO cancelOrder(String orderId);
}
