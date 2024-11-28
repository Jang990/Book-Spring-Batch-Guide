package io.spring.batch.hello_world.chpater9._2;

import io.spring.batch.hello_world.chpater9.Chapter9_Customer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerItemPreparedStatementSetter implements ItemPreparedStatementSetter<Chapter9_Customer> {
    @Override
    public void setValues(Chapter9_Customer item, PreparedStatement ps) throws SQLException {
        ps.setString(1,item.getFirstName());
        ps.setString(2,item.getMiddleInitial());
        ps.setString(3,item.getLastName());
        ps.setString(4,item.getAddress());
        ps.setString(5,item.getCity());
        ps.setString(6,item.getState());
        ps.setString(7,item.getZip());
    }
}
