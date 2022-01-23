package com.nagarro.account.statement.service;

import com.nagarro.account.statement.model.StatementResponse;

public interface StatementService {

    StatementResponse getStatementByAmount(int accountId, Double fromAmount, Double toAmount);
    StatementResponse getStatementFromDate(int accountId, String fromDate, String toDate);
    StatementResponse getLastThreeMonthsStatement(int accountId);
}
