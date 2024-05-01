package com.bankapp.util;

import com.bankapp.model.Account;
import com.bankapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Component
public class DataSeeder {

    private final AccountRepository accountRepository;

    @Autowired
    public DataSeeder(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void seedData() {
        seedAccounts();

    }

    private void seedAccounts() {
        // Check if data already exists, it shouldn't but helps to check...
        if (accountRepository.count() == 0) {
            Account account1 = new Account("1", new BigDecimal("1000.00"));
            Account account2 = new Account("2", new BigDecimal("500.00"));

            accountRepository.save(account1);
            accountRepository.save(account2);
        }
    }
}
