package com.nagarro.account.statement.service.impl;

import com.nagarro.account.statement.exceptions.GenericRuntimeException;
import com.nagarro.account.statement.model.AccountDetail;
import com.nagarro.account.statement.model.StatementResponse;
import com.nagarro.account.statement.repository.AccountRepository;
import com.nagarro.account.statement.repository.StatementRepository;
import com.nagarro.account.statement.service.StatementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private StatementRepository statementRepository;
    private AccountRepository accountRepository;

    private static final String ERROR_MSG_1 = "Account Id {} not found";
    private static final String ERROR_MSG_2 = "Account Id not found";


    @Override
    public StatementResponse getStatementByAmount(int accountId, Double fromAmount, Double toAmount){

        List<AccountDetail> accountDetailsList = accountRepository.findByAccountId(accountId);
        if(accountDetailsList.isEmpty()){
            log.error(ERROR_MSG_1,accountId);
            throw new GenericRuntimeException(ERROR_MSG_2);
        }

        return StatementResponse.builder().accountNumber(accountDetailsList.get(0).getAccountNumber().hashCode()).
                statementDetailList(statementRepository.findByAccountId(accountId).stream().
                filter(statement -> statement.getAmount() >= fromAmount && statement.getAmount() <= toAmount)
                .collect(Collectors.toList())).build();

    }

    @Override
    public StatementResponse getStatementFromDate(int accountId, String fromDate, String toDate){

        List<AccountDetail> accountDetailsList = accountRepository.findByAccountId(accountId);
        if(accountDetailsList.isEmpty()){
            log.error(ERROR_MSG_1,accountId);
            throw new GenericRuntimeException(ERROR_MSG_2);
        }

        LocalDate startDate = formatDate(fromDate);
        LocalDate endDate = formatDate(toDate);
        return StatementResponse.builder().accountNumber(accountDetailsList.get(0).getAccountNumber().hashCode()).
                statementDetailList(statementRepository.findByAccountId(accountId).stream().
                        filter(statement -> statement.getDate().compareTo(startDate) >= 0 && statement.getDate().compareTo(endDate) <= 0)
                        .collect(Collectors.toList())).build();

    }

    @Override
    public StatementResponse getLastThreeMonthsStatement(int accountId) {

        List<AccountDetail> accountDetailsList = accountRepository.findByAccountId(accountId);
        if(accountDetailsList.isEmpty()){
            log.error(ERROR_MSG_1,accountId);
            throw new GenericRuntimeException(ERROR_MSG_2);
        }

        Calendar requiredDate = Calendar.getInstance();
        requiredDate.add(Calendar.MONTH, -3);
        LocalDate compareDate = requiredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return StatementResponse.builder().accountNumber(accountDetailsList.get(0).getAccountNumber().hashCode()).
                statementDetailList(statementRepository.findByAccountId(accountId).stream().filter(statement -> compareDate.compareTo(statement.getDate()) <= 0).collect(Collectors.toList())).build();

    }

    private LocalDate formatDate(String date){
        return LocalDate.parse(date,DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
