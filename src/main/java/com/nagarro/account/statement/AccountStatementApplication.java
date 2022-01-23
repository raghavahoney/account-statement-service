package com.nagarro.account.statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nagarro.account.statement"})
public class AccountStatementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountStatementApplication.class, args);
    }
}
