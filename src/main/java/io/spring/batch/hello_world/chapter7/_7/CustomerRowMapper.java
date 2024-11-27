package io.spring.batch.hello_world.chapter7._7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_JDBC_Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerRowMapper implements RowMapper<Chapter7_JDBC_Customer> {

    @Override
    public Chapter7_JDBC_Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chapter7_JDBC_Customer customer = new Chapter7_JDBC_Customer();

        customer.setId(rs.getLong("id"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setFirstName(rs.getString("firstName"));
        customer.setLastName(rs.getString("lastName"));
        customer.setMiddleInitial(rs.getString("middleInitial"));
        customer.setState(rs.getString("state"));
        customer.setZipCode(rs.getString("zipCode"));

        return customer;
    }
}
