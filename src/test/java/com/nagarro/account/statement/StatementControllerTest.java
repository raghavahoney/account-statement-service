package com.nagarro.account.statement;

import static org.mockito.Mockito.when;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.nagarro.account.statement.exceptions.GenericRuntimeException;
import com.nagarro.account.statement.model.AccountDetail;
import com.nagarro.account.statement.model.StatementDetail;
import com.nagarro.account.statement.model.StatementResponse;
import com.nagarro.account.statement.repository.AccountRepository;
import com.nagarro.account.statement.repository.StatementRepository;
import com.nagarro.account.statement.service.StatementService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.anyString;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatementControllerTest {

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private StatementService statementService;

    @Test(expected = GenericRuntimeException.class)
    public void testCommonExceptionScenario() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenThrow(RuntimeException.class);
        statementService.getStatementByAmount(1, 3.0d, 3.0d);

    }

    @Test
    public void testAccountStatementByAmount() {
        List<StatementDetail> response = new ArrayList<>();
        List<AccountDetail> accountDetailsList = new ArrayList<>();
        accountDetailsList.add(AccountDetail.builder().accountNumber("123456").accountType("Savings").build());
        response.add(new StatementDetail(LocalDate.now().minusDays(5L), 100.0d));
        response.add(new StatementDetail(LocalDate.now().minusDays(1L), 100.0d));

        StatementResponse statementResponse = StatementResponse.builder().accountNumber(123456).statementDetailList(response).build();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(response);
        when(accountRepository.findByAccountId(any())).thenReturn(accountDetailsList);

        StatementResponse statement  = statementService.getStatementByAmount(1,3.0d, 3.0d);
        Assert.assertEquals(0, statement.getStatementDetailList().size());
    }

    @Test
    public void testAccountStatementByDateRange() {
        List<StatementDetail> response = new ArrayList<>();
        List<AccountDetail> accountDetailsList = new ArrayList<>();
        accountDetailsList.add(AccountDetail.builder().accountNumber("123456").accountType("Savings").build());

        response.add(new StatementDetail(LocalDate.now().minusDays(5L), 100.0d));
        response.add(new StatementDetail(LocalDate.now().minusDays(1L), 100.0d));

        StatementResponse statementResponse = StatementResponse.builder().accountNumber(123456).statementDetailList(response).build();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(response);
        when(accountRepository.findByAccountId(any())).thenReturn(accountDetailsList);

        StatementResponse statement  = statementService.getStatementFromDate(1,"22.01.2010", "23.01.2022");
        Assert.assertEquals(0, statement.getStatementDetailList().size());
    }
}