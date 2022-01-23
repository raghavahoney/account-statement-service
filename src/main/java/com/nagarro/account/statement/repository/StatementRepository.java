package com.nagarro.account.statement.repository;

import com.nagarro.account.statement.model.StatementDetail;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class StatementRepository{

    private JdbcTemplate template;

    public List<StatementDetail> findByAccountId(int accountId) {
        String sql = "select ID,account_id,DateValue(Replace(datefield,'.','/') ) as datefield ,amount from statement where account_id=?";
        return template.query(sql, new StatementRowMapper(), accountId);
    }

    private class StatementRowMapper implements RowMapper<StatementDetail> {
        @Override
        public StatementDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            return StatementDetail.builder()
                    .date(rs.getDate("datefield").toLocalDate())
                    .amount(rs.getDouble("amount")).build();
        }
    }
}
