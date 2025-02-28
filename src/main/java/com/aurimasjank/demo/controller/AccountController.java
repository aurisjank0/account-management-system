package com.aurimasjank.demo.controller;

import com.aurimasjank.demo.dto.CreateAccountDTO;
import com.aurimasjank.demo.dto.TransactionDTO;
import com.aurimasjank.demo.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create a new account")
    @PostMapping
    public void createAccount(@RequestBody CreateAccountDTO account) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null.");
        }
        if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account number must not be empty.");
        }
        if (account.getCustomerId() == null || account.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive.");
        }
        accountService.createAccount(account);
    }

    @Operation(summary = "Deposit money into an account")
    @PostMapping("/{accountId}/deposit")
    public void deposit(@PathVariable Long accountId, @RequestParam double amount) {
        validatePositive(accountId, "Account ID");
        validatePositive(amount, "Amount");
        accountService.deposit(accountId, amount);
    }

    @Operation(summary = "Withdraw money from an account")
    @PostMapping("/{accountId}/withdraw")
    public void withdraw(@PathVariable Long accountId, @RequestParam double amount) {
        validatePositive(accountId, "Account ID");
        validatePositive(amount, "Amount");
        accountService.withdraw(accountId, amount);
    }

    @GetMapping("/{accountId}/balance")
    public double getBalance(@PathVariable Long accountId) {
        validatePositive(accountId, "Account ID");
        return accountService.getBalance(accountId);
    }

    @Operation(summary = "Get last 10 transactions")
    @GetMapping("/{accountId}/transactions")
    public List<TransactionDTO> getLast10Transactions(@PathVariable Long accountId) {
        validatePositive(accountId, "Account ID");
        return accountService.getLast10Transactions(accountId);
    }

    private void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive.");
        }
    }

    private void validatePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive.");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}