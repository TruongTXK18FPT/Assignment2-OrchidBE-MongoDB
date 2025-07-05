package com.example.orchidservice.service;

import com.example.orchidservice.dto.LoginRequestDTO;
import com.example.orchidservice.dto.LoginResponseDTO;
import com.example.orchidservice.dto.RegisterRequestDTO;
import com.example.orchidservice.dto.RegisterResponseDTO;
import com.example.orchidservice.dto.UpdateProfileRequestDTO;
import com.example.orchidservice.dto.ForgotPasswordRequestDTO;
import com.example.orchidservice.dto.ResetPasswordRequestDTO;
import com.example.orchidservice.dto.AccountDTO;
import com.example.orchidservice.pojo.Account;
import com.example.orchidservice.pojo.Role;
import com.example.orchidservice.repository.AccountRepository;
import com.example.orchidservice.repository.RoleRepository;
import com.example.orchidservice.service.imp.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(String id) {
        if (!accountRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        accountRepository.deleteById(id);
    }

    @Override
    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public List<Account> getAccountsByRoleId(String roleId) {
        return accountRepository.findByRoleRoleId(roleId);
    }

    @Override
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        // Check if email already exists
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        Role defaultRole = roleRepository.findById("3")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default user role not found"));

        Account newAccount = new Account();
        newAccount.setAccountName(request.getAccountName());
        newAccount.setEmail(request.getEmail());
        newAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        newAccount.setRole(defaultRole);

        Account savedAccount = accountRepository.save(newAccount);

        return RegisterResponseDTO.builder()
                .accountId(savedAccount.getAccountId())
                .accountName(savedAccount.getAccountName())
                .email(savedAccount.getEmail())
                .roleId(savedAccount.getRole().getRoleId())
                .build();
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(account);

        return LoginResponseDTO.builder()
                .token(token)
                .accountId(account.getAccountId())
                .accountName(account.getAccountName())
                .email(account.getEmail())
                .roleId(account.getRole().getRoleId())
                .roleName(account.getRole().getRoleName())
                .build();
    }

    @Override
    @Transactional
    public void logout(String token) {
        // In a real implementation, you would invalidate the token
        // For now, we'll just log the logout action
        // You could store invalidated tokens in a blacklist
    }

    @Override
    @Transactional
    public AccountDTO updateProfile(Account currentUser, UpdateProfileRequestDTO request) {
        // Only update account name
        currentUser.setAccountName(request.getAccountName());

        Account updatedAccount = accountRepository.save(currentUser);

        return AccountDTO.builder()
                .accountId(updatedAccount.getAccountId())
                .accountName(updatedAccount.getAccountName())
                .email(updatedAccount.getEmail())
                .roleId(updatedAccount.getRole().getRoleId())
                .roleName(updatedAccount.getRole().getRoleName())
                .build();
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequestDTO request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        // In a real implementation, you would send an email with reset token
        // For now, we'll just log the action
        // You could generate a reset token and store it temporarily
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequestDTO request) {
        // In a real implementation, you would validate the reset token
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    @Transactional
    public Account registerAccount(RegisterRequestDTO registerRequestDTO) {
        Role defaultRole = roleRepository.findById("3")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default user role not found"));

        Account newAccount = new Account();
        newAccount.setAccountName(registerRequestDTO.getAccountName());
        newAccount.setEmail(registerRequestDTO.getEmail());
        newAccount.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        newAccount.setRole(defaultRole);

        return accountRepository.save(newAccount);
    }
}

