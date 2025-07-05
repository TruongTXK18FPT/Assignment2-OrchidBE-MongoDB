package com.example.orchidservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.orchidservice.pojo.Orchid;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrchidRepository extends MongoRepository<Orchid, String> {
    List<Orchid> findByCategoryCategoryId(String categoryId);
    List<Orchid> findByOrchidNameContainingIgnoreCase(String name);
    List<Orchid> findByPriceBetween(String minPrice, String maxPrice);
    List<Orchid> findByIsNatural(Boolean isNatural);
}