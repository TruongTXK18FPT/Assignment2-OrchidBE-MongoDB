package com.example.orchidservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String accountId;
    private String accountName;
    private String email;
    private String roleId;
    private String roleName;
    private String message;
    private boolean success;
    private String token; // For future JWT implementation
}
