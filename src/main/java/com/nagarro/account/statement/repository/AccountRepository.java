package com.nagarro.account.statement.repository;

import com.nagarro.account.statement.model.AccountDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class AccountRepository {

    @Autowired
    private JdbcTemplate template;

    public List<AccountDetail> findAll() {
        return template.query("select ID,account_type,account_number from account",new AccountRowMapper());
    }

    public List<AccountDetail> findByAccountId(Integer accountId) {
        return template.query("select ID,account_type,account_number from account where id=? ", new AccountRowMapper(),accountId);
    }

    private class AccountRowMapper implements RowMapper<AccountDetail> {
        @Override
        public AccountDetail mapRow(ResultSet rs, int i) throws SQLException {
            return AccountDetail.builder().
                    id(rs.getInt("id"))
                    .accountType(rs.getString("account_type"))
                    .accountNumber(rs.getString("account_number"))
                    .build();
        }
    }
}
