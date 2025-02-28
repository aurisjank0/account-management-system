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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        account = new Account();
        account.setId(1L);
        account.setCustomer(customer);
        account.setBalance(0);
    }

    @Test
    void createAccount() {
        CreateAccountDTO accountDTO = new CreateAccountDTO();
        accountDTO.setCustomerId(1L);
        accountDTO.setAccountNumber("123456");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        accountService.createAccount(accountDTO);

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_CustomerNotFound() {
        CreateAccountDTO accountDTO = new CreateAccountDTO();
        accountDTO.setCustomerId(1L);
        accountDTO.setAccountNumber("123456");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> accountService.createAccount(accountDTO));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void deposit() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        account.setBalance(100);
        accountService.deposit(1L, 50.0);

        assertEquals(150.0, account.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void deposit_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deposit(1L, 50.0));
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void withdraw() {
        account.setBalance(100);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.withdraw(1L, 50.0);

        assertEquals(50.0, account.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void withdraw_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.withdraw(1L, 50.0));

        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void withdraw_InsufficientBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class, () -> accountService.withdraw(1L, 150.0));
    }

    @Test
    void getBalance() {
        account.setBalance(100);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        double balance = accountService.getBalance(1L);

        assertEquals(100.0, balance);
    }

    @Test
    void getLast10Transactions() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccount(account);
        transaction.setAmount(50.0);
        LocalDateTime now = LocalDateTime.now();
        transaction.setTimestamp(now);
        transaction.setType(TransactionType.DEPOSIT);
        account.setTransactions(List.of(transaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.findTop10ByAccountOrderByTimestampDesc(account)).thenReturn(List.of(transaction));

        List<TransactionDTO> transactions = accountService.getLast10Transactions(1L);

        assertEquals(1, transactions.size());
        assertEquals(50.0, transactions.get(0).getAmount());
    }

    @Test
    void getLast10Transactions_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getLast10Transactions(1L));
    }
}