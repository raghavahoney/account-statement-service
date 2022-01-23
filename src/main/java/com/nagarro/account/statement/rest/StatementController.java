package com.nagarro.account.statement.rest;

import com.nagarro.account.statement.config.ExceptionControllerAdvice;
import com.nagarro.account.statement.exceptions.BadRequestParamterException;
import com.nagarro.account.statement.model.StatementResponse;
import com.nagarro.account.statement.service.StatementService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@AllArgsConstructor
@Slf4j
@Api(tags = "Account Statement Details")
public class StatementController extends ExceptionControllerAdvice {

    private StatementService statementService;

    @GetMapping(value = "/account/statement/{accountId}", params = {"fromAmount", "toAmount"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StatementResponse> getAccountStatmentByAmount(
            @PathVariable Integer accountId,
            @NotBlank @RequestParam(value = "fromAmount") String fromAmount,
            @NotBlank @RequestParam(value = "toAmount") String toAmount) {

        validateAmountFields(fromAmount, toAmount);
        return ResponseEntity.ok(statementService.getStatementByAmount(accountId, Double.valueOf(fromAmount), Double.valueOf(toAmount)));
    }

    @GetMapping(value = "/account/statement/{accountId}", params = {"fromDate","toDate"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StatementResponse> getAccountStatmentByDate(
            @PathVariable Integer accountId,
            @Valid @NotBlank @RequestParam(value = "fromDate") String fromDate,
            @Valid @NotBlank @RequestParam(value = "toDate") String toDate) {

        validateDateFields(fromDate,toDate);
        return ResponseEntity.ok(statementService.getStatementFromDate(accountId, fromDate, toDate));
    }

    @GetMapping(value = "/account/statement/{accountId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<StatementResponse> statementLastThreeMonth(@PathVariable Integer accountId) {
        return ResponseEntity.ok(statementService.getLastThreeMonthsStatement(accountId));
    }

    private void validateAmountFields(String fromAmount, String toAmount){
        try {
            Double.parseDouble(fromAmount);
        } catch (Exception e) {
            throw new BadRequestParamterException("Invalid From Amount");
        }

        try {
            Double.parseDouble(toAmount);
        } catch (Exception e) {
            throw new BadRequestParamterException("Invalid To Amount");
        }
    }

    private void validateDateFields(String fromDate, String toDate){
        try {
            LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (Exception e) {
            throw new BadRequestParamterException("Invalid From Date. Format Expected is dd.MM.yyyy");
        }

        try {
            LocalDate.parse(toDate,DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (Exception e) {
            throw new BadRequestParamterException("Invalid To Date. Format Expected is dd.MM.yyyy");
        }
    }

}
