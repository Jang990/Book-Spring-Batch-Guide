package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_JDBC_Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class _7_JdbcCursorConfigTest {
    @Autowired
    DataSource dataSource;

    _7_JdbcCursorConfig readerConfig = new _7_JdbcCursorConfig();

    @Test
    @DisplayName("DB에서 읽어올 수 있는지 확인")
    void test1() throws Exception {
        JdbcCursorItemReader<Chapter7_JDBC_Customer> reader = readerConfig.customerJdbcItemReader(dataSource);
        reader.open(new ExecutionContext());
        System.out.println(reader.read());
    }

    @Test
    @DisplayName("전체 읽기")
    void test2() throws Exception {
        JdbcCursorItemReader<Chapter7_JDBC_Customer> reader = readerConfig.customerJdbcItemReader(dataSource);
        reader.open(new ExecutionContext());

        for (int i = 0; i < 1000; i++) {
            System.out.println(reader.read());
        }
    }

}