package com.aurimasjank.demo.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    private Long id;
    private Long accountId;
    private double amount;
    private String type;
    private String timestamp;
}