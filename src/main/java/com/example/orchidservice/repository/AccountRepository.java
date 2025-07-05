package com.example.orchidservice.repository;

import com.example.orchidservice.pojo.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    List<Account> findByRoleRoleId(String roleId);

    @Query("{ 'email': ?0 }")
    Optional<Account> findByEmailWithRole(String email);
}