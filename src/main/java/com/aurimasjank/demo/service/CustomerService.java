package com.aurimasjank.demo.service;

import com.aurimasjank.demo.dto.AccountDTO;
import com.aurimasjank.demo.dto.CustomerDTO;
import com.aurimasjank.demo.exception.CustomerNotFoundException;
import com.aurimasjank.demo.model.Account;
import com.aurimasjank.demo.model.Customer;
import com.aurimasjank.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long customerId) {
        return convertToDTO(customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found")));
    }

    public List<AccountDTO> getCustomerAccounts(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        return customer.getAccounts().stream()
                .map(this::convertToAccountDTO)
                .collect(Collectors.toList());
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        return customerDTO;
    }

    private AccountDTO convertToAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setBalance(account.getBalance());
        return accountDTO;
    }

}