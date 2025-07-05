package com.example.orchidservice.repository;

import com.example.orchidservice.pojo.OrderDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends MongoRepository<OrderDetail, String> {
    List<OrderDetail> findByOrderId(String orderId);
    List<OrderDetail> findByOrchidOrchidId(String orchidId);
}