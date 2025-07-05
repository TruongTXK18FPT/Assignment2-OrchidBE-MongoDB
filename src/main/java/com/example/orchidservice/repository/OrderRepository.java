package com.example.orchidservice.repository;

import com.example.orchidservice.pojo.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByAccountAccountId(String accountId);
    List<Order> findByOrderStatus(String orderStatus);
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    List<Order> findByOrderDate(LocalDate orderDate);
}