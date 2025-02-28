package com.aurimasjank.demo.controller;

import com.aurimasjank.demo.dto.AccountDTO;
import com.aurimasjank.demo.dto.CustomerDTO;
import com.aurimasjank.demo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Operation(summary = "Get customer by id")
    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable Long customerId) {
        validatePositive(customerId, "Customer ID");
        return customerService.getCustomerById(customerId);
    }

    @Operation(summary = "Get customer accounts")
    @GetMapping("/{customerId}/accounts")
    public List<AccountDTO> getCustomerAccounts(@PathVariable Long customerId) {
        validatePositive(customerId, "Customer ID");
        return customerService.getCustomerAccounts(customerId);
    }

    private void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive.");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}