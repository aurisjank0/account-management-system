package com.aurimasjank.demo.service;

import com.aurimasjank.demo.dto.CreateAccountDTO;
import com.aurimasjank.demo.dto.TransactionDTO;
import com.aurimasjank.demo.exception.AccountNotFoundException;
import com.aurimasjank.demo.exception.CustomerNotFoundException;
import com.aurimasjank.demo.exception.InsufficientBalanceException;
import com.aurimasjank.demo.model.Account;
import com.aurimasjank.demo.model.Customer;
import com.aurimasjank.demo.model.Transaction;
import com.aurimasjank.demo.model.TransactionType;
import com.aurimasjank.demo.repository.AccountRepository;
import com.aurimasjank.demo.repository.CustomerRepository;
import com.aurimasjank.demo.repository.TransactionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final CustomerRepository customerRepository;

    @Transactional
    public void createAccount(@Valid CreateAccountDTO accountDTO) {
        Customer customer = customerRepository.findById(accountDTO.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setCustomer(customer);
        accountRepository.save(account);
    }

    @Transactional
    public void deposit(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    public double getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return account.getBalance();
    }

    public List<TransactionDTO> getLast10Transactions(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return transactionRepository.findTop10ByAccountOrderByTimestampDesc(account).stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO convertToTransactionDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setAccountId(transaction.getAccount().getId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setType(transaction.getType().name());
        transactionDTO.setTimestamp(transaction.getTimestamp().toString());
        return transactionDTO;
    }
}