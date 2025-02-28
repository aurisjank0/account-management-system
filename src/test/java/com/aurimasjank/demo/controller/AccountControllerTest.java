package com.aurimasjank.demo.controller;

import com.aurimasjank.demo.dto.TransactionDTO;
import com.aurimasjank.demo.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void testCreateAccountWithValidInput() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"12345\", \"customerId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateAccountWithNullAccount() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateAccountWithEmptyAccountNumber() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"\", \"customerId\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateAccountWithNullCustomerId() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"12345\", \"customerId\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateAccountWithNegativeCustomerId() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"12345\", \"customerId\":-1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeposit() throws Exception {
        mockMvc.perform(post("/accounts/1/deposit")
                        .param("amount", "100"))
                .andExpect(status().isOk());
    }

    @Test
    void testWithdraw() throws Exception {
        mockMvc.perform(post("/accounts/1/withdraw")
                        .param("amount", "50"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBalance() throws Exception {
        when(accountService.getBalance(1L)).thenReturn(100.0);
        mockMvc.perform(get("/accounts/1/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }

    @Test
    void testGetLast10Transactions() throws Exception {
        List<TransactionDTO> transactions = Collections.emptyList();
        when(accountService.getLast10Transactions(1L)).thenReturn(transactions);
        mockMvc.perform(get("/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testDepositWithNegativeAccountId() throws Exception {
        mockMvc.perform(post("/accounts/-1/deposit")
                        .param("amount", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDepositWithNegativeAmount() throws Exception {
        mockMvc.perform(post("/accounts/1/deposit")
                        .param("amount", "-100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDepositWithZeroAmount() throws Exception {
        mockMvc.perform(post("/accounts/1/deposit")
                        .param("amount", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWithdrawWithNegativeAccountId() throws Exception {
        mockMvc.perform(post("/accounts/-1/withdraw")
                        .param("amount", "50"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWithdrawWithNegativeAmount() throws Exception {
        mockMvc.perform(post("/accounts/1/withdraw")
                        .param("amount", "-50"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWithdrawWithZeroAmount() throws Exception {
        mockMvc.perform(post("/accounts/1/withdraw")
                        .param("amount", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBalanceWithNegativeAccountId() throws Exception {
        mockMvc.perform(get("/accounts/-1/balance"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBalanceWithZeroAccountId() throws Exception {
        mockMvc.perform(get("/accounts/0/balance"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetLast10TransactionsWithNegativeAccountId() throws Exception {
        mockMvc.perform(get("/accounts/-1/transactions"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetLast10TransactionsWithZeroAccountId() throws Exception {
        mockMvc.perform(get("/accounts/0/transactions"))
                .andExpect(status().isBadRequest());
    }
}