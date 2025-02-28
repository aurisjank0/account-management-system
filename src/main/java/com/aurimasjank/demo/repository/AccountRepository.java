package com.aurimasjank.demo.repository;

import com.aurimasjank.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
