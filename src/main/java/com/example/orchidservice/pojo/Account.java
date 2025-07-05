package com.example.orchidservice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Document(collection = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private String accountId;

    private String accountName;

    @Indexed(unique = true)
    private String email;

    private String password;

    @DBRef
    private Role role;

    @DBRef
    @JsonIgnore
    private List<Order> orders;

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}