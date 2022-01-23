package com.nagarro.account.statement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementResponse {

    private Integer accountNumber;
    private List<StatementDetail> statementDetailList;
}
