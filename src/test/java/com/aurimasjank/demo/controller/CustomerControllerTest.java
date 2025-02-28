package com.aurimasjank.demo.controller;

import com.aurimasjank.demo.dto.AccountDTO;
import com.aurimasjank.demo.dto.CustomerDTO;
import com.aurimasjank.demo.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        CustomerDTO customer1 = new CustomerDTO(1L, "John Doe");
        CustomerDTO customer2 = new CustomerDTO(2L, "Jane Doe");

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer1, customer2));

        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'name':'John Doe'},{'id':2,'name':'Jane Doe'}]"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        CustomerDTO customer = new CustomerDTO(1L, "John Doe");

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'name':'John Doe'}"));
    }

    @Test
    void testGetCustomerById_withNegativeId() throws Exception {
        mockMvc.perform(get("/customers/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCustomerAccounts() throws Exception {
        AccountDTO account1 = new AccountDTO(1L, "123456", 1000.0);
        AccountDTO account2 = new AccountDTO(2L, "654321", 2000.0);

        when(customerService.getCustomerAccounts(1L)).thenReturn(Arrays.asList(account1, account2));

        mockMvc.perform(get("/customers/1/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'accountNumber':'123456','balance':1000.0},{'id':2,'accountNumber':'654321','balance':2000.0}]"));
    }

    @Test
    void testGetCustomerAccounts_withNegativeId() throws Exception {
        mockMvc.perform(get("/customers/-1/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}