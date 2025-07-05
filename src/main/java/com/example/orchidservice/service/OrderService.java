package com.example.orchidservice.service;

import com.example.orchidservice.dto.OrderDTO;
import com.example.orchidservice.dto.OrderDetailDTO;
import com.example.orchidservice.pojo.Order;
import com.example.orchidservice.pojo.OrderDetail;
import com.example.orchidservice.pojo.Account;
import com.example.orchidservice.pojo.Orchid;
import com.example.orchidservice.repository.OrderDetailRepository;
import com.example.orchidservice.repository.OrderRepository;
import com.example.orchidservice.repository.AccountRepository;
import com.example.orchidservice.repository.OrchidRepository;
import com.example.orchidservice.service.imp.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrchidRepository orchidRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> getOrderById(String id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);

        // Set order date if not provided
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDate.now());
        }

        // Calculate total amount
        String totalAmount = calculateTotalFromDetails(orderDTO.getOrderDetails());
        order.setTotalAmount(totalAmount);

        // Save the order first (without order details)
        order.setOrderDetails(null); // Temporarily set to null
        Order savedOrder = orderRepository.save(order);

        // Create and save order details
        List<OrderDetail> orderDetails = new ArrayList<>();
        if (orderDTO.getOrderDetails() != null && !orderDTO.getOrderDetails().isEmpty()) {
            for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setPrice(detailDTO.getPrice());
                orderDetail.setQuantity(detailDTO.getQuantity());
                orderDetail.setOrder(savedOrder);

                // Set the orchid reference
                if (detailDTO.getOrchidId() != null) {
                    Orchid orchid = orchidRepository.findById(detailDTO.getOrchidId())
                            .orElseThrow(() -> new RuntimeException("Orchid not found: " + detailDTO.getOrchidId()));
                    orderDetail.setOrchid(orchid);
                }

                // Save each order detail
                OrderDetail savedDetail = orderDetailRepository.save(orderDetail);
                orderDetails.add(savedDetail);
            }
        }

        // Update the order with the saved order details
        savedOrder.setOrderDetails(orderDetails);
        Order finalOrder = orderRepository.save(savedOrder);

        return convertToDTO(finalOrder);
    }

    @Override
    public OrderDTO updateOrder(String id, OrderDTO orderDTO) {
        Optional<Order> existing = orderRepository.findById(id);
        if (existing.isPresent()) {
            Order order = existing.get();
            order.setOrderStatus(orderDTO.getOrderStatus());

            // Update account if provided
            if (orderDTO.getAccountId() != null) {
                Account account = accountRepository.findById(orderDTO.getAccountId())
                        .orElseThrow(() -> new RuntimeException("Account not found"));
                order.setAccount(account);
            }

            // Recalculate total if order details are updated
            if (orderDTO.getOrderDetails() != null) {
                String totalAmount = calculateTotalFromDetails(orderDTO.getOrderDetails());
                order.setTotalAmount(totalAmount);
            }

            Order updated = orderRepository.save(order);
            return convertToDTO(updated);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    @Override
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDTO> getOrdersByAccount(String accountId) {
        return orderRepository.findByAccountAccountId(accountId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderRepository.findByOrderStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderStatus(String id, String status) {
        Optional<Order> existing = orderRepository.findById(id);
        if (existing.isPresent()) {
            Order order = existing.get();
            order.setOrderStatus(status);
            Order updated = orderRepository.save(order);
            return convertToDTO(updated);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    @Override
    public String calculateOrderTotal(String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get().getOrderDetails().stream()
                    .map(detail -> {
                        double price = Double.parseDouble(detail.getPrice());
                        int quantity = Integer.parseInt(detail.getQuantity());
                        return price * quantity;
                    })
                    .reduce(0.0, Double::sum)
                    .toString();
        }
        throw new RuntimeException("Order not found with id: " + orderId);
    }

    @Override
    public List<OrderDetailDTO> getOrderDetailsByOrderId(String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get().getOrderDetails().stream()
                    .map(this::convertOrderDetailToDTO)
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Order not found with id: " + orderId);
    }

    @Override
    public Optional<OrderDetailDTO> getOrderDetailById(String orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .map(this::convertOrderDetailToDTO);
    }

    @Override
    public OrderDTO cancelOrder(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }

        Order order = orderOpt.get();

        // Check if order can be cancelled - only pending and processing orders
        if (!"pending".equals(order.getOrderStatus()) && !"processing".equals(order.getOrderStatus())) {
            throw new RuntimeException("Can only cancel orders with status 'pending' or 'processing'");
        }

        // Update order status to cancelled
        order.setOrderStatus("cancelled");
        Order savedOrder = orderRepository.save(order);

        return convertToDTO(savedOrder);
    }

    private String calculateTotalFromDetails(List<OrderDetailDTO> orderDetails) {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return "0.0";
        }
        double total = orderDetails.stream()
                .mapToDouble(detail -> {
                    double price = Double.parseDouble(detail.getPrice());
                    int quantity = Integer.parseInt(detail.getQuantity());
                    return price * quantity;
                })
                .sum();
        return String.valueOf(total);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotalAmount(order.getTotalAmount());

        if (order.getAccount() != null) {
            dto.setAccountId(order.getAccount().getAccountId());
            dto.setAccountName(order.getAccount().getAccountName());
        }

        if (order.getOrderDetails() != null) {
            List<OrderDetailDTO> orderDetailDTOs = order.getOrderDetails().stream()
                    .map(this::convertOrderDetailToDTO)
                    .collect(Collectors.toList());
            dto.setOrderDetails(orderDetailDTOs);
        }

        return dto;
    }

    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderDate(dto.getOrderDate());
        order.setOrderStatus(dto.getOrderStatus());
        order.setTotalAmount(dto.getTotalAmount());

        if (dto.getAccountId() != null) {
            Account account = accountRepository.findById(dto.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            order.setAccount(account);
        }

        return order;
    }

    private OrderDetailDTO convertOrderDetailToDTO(OrderDetail orderDetail) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(orderDetail.getId());
        dto.setPrice(orderDetail.getPrice());
        dto.setQuantity(orderDetail.getQuantity());
        dto.setOrderId(orderDetail.getOrder().getId());

        // Calculate subtotal by parsing strings to numbers
        double price = Double.parseDouble(orderDetail.getPrice());
        int quantity = Integer.parseInt(orderDetail.getQuantity());
        dto.setSubtotal(String.valueOf(price * quantity));

        if (orderDetail.getOrchid() != null) {
            dto.setOrchidId(orderDetail.getOrchid().getOrchidId());
            dto.setOrchidName(orderDetail.getOrchid().getOrchidName());
            dto.setOrchidUrl(orderDetail.getOrchid().getOrchidUrl());
        }

        return dto;
    }
}

