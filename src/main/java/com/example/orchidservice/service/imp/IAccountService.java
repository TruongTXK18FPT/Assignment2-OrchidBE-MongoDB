package com.example.orchidservice.service.imp;

import com.example.orchidservice.dto.RegisterRequestDTO;
import com.example.orchidservice.dto.RegisterResponseDTO;
import com.example.orchidservice.dto.LoginRequestDTO;
import com.example.orchidservice.dto.LoginResponseDTO;
import com.example.orchidservice.dto.UpdateProfileRequestDTO;
import com.example.orchidservice.dto.ForgotPasswordRequestDTO;
import com.example.orchidservice.dto.ResetPasswordRequestDTO;
import com.example.orchidservice.dto.AccountDTO;
import com.example.orchidservice.pojo.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<Account> getAllAccounts();
    Optional<Account> getAccountById(String id);
    Account saveAccount(Account account);
    void deleteAccount(String id);
    Optional<Account> getAccountByEmail(String email);
    List<Account> getAccountsByRoleId(String roleId);
    RegisterResponseDTO register(RegisterRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);
    void logout(String token);
    AccountDTO updateProfile(Account currentUser, UpdateProfileRequestDTO request);
    void forgotPassword(ForgotPasswordRequestDTO request);
    void resetPassword(ResetPasswordRequestDTO request);
}
