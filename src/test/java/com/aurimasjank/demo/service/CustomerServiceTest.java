package com.aurimasjank.demo.service;


import com.aurimasjank.demo.dto.AccountDTO;
import com.aurimasjank.demo.dto.CustomerDTO;
import com.aurimasjank.demo.exception.CustomerNotFoundException;
import com.aurimasjank.demo.model.Account;
import com.aurimasjank.demo.model.Customer;
import com.aurimasjank.demo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("test1");
        customer1.setAccounts(new ArrayList<>());

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("test2");
        customer2.setAccounts(new ArrayList<>());

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<CustomerDTO> customers = customerService.getAllCustomers();

        assertEquals(2, customers.size());
        assertEquals("test1", customers.get(0).getName());
        assertEquals("test2", customers.get(1).getName());
    }

    @Test
    void testGetCustomerById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("test1");
        customer.setAccounts(new ArrayList<>());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO customerDTO = customerService.getCustomerById(1L);

        assertEquals(1L, customerDTO.getId());
        assertEquals("test1", customerDTO.getName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void testGetCustomerAccounts() {
        Customer customer = new Customer();
        customer.setId(1L);

        Account account1 = new Account();
        account1.setId(1L);
        account1.setAccountNumber("123456");
        account1.setBalance(1000.0);
        account1.setTransactions(new ArrayList<>());

        Account account2 = new Account();
        account2.setId(2L);
        account2.setAccountNumber("654321");
        account2.setBalance(2000.0);
        account2.setTransactions(new ArrayList<>());

        customer.setAccounts(Arrays.asList(account1, account2));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        List<AccountDTO> accounts = customerService.getCustomerAccounts(1L);

        assertEquals(2, accounts.size());
        assertEquals("123456", accounts.get(0).getAccountNumber());
        assertEquals("654321", accounts.get(1).getAccountNumber());
    }

    @Test
    void testGetCustomerAccounts_noAccounts() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setAccounts(new ArrayList<>());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        List<AccountDTO> accounts = customerService.getCustomerAccounts(1L);

        assertEquals(0, accounts.size());
    }
}
