package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._7.CustomerRowMapper;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_JDBC_Customer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

import javax.sql.DataSource;

public class _7_JdbcCursorConfig {
    // 한 번에 하나의 레코드 처리
    @Bean
    public JdbcCursorItemReader<Chapter7_JDBC_Customer> customerJdbcItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Chapter7_JDBC_Customer>()
                .name("CustomerJdbcItemReader")
                .dataSource(dataSource)
                .sql("select * from CH7_CUSTOMER where city = ?")
                .rowMapper(new CustomerRowMapper())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Chapter7_JDBC_Customer> cityCustomerJdbcItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Chapter7_JDBC_Customer>()
                .name("CustomerJdbcItemReader")
                .dataSource(dataSource)
                .sql("select * from CH7_CUSTOMER where city = ?")
                .rowMapper(new CustomerRowMapper())
                .preparedStatementSetter(citySetter(null))
                .build();
    }

    @Bean
    @StepScope
    public ArgumentPreparedStatementSetter citySetter(@Value("#{jobParameter['city']}") String city) {
        return new ArgumentPreparedStatementSetter(new Object[]{city});
    }
}
