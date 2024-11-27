package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_JDBC_Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class _7_JdbcPagingConfigTest {

    @Autowired
    DataSource dataSource;

    _7_JdbcPagingConfig readerConfig = new _7_JdbcPagingConfig();

    @Test
    @DisplayName("DB에서 읽어올 수 있는지 확인")
    void test1() throws Exception {
        JdbcPagingItemReader<Chapter7_JDBC_Customer> reader = getReader("Juneau");
        reader.afterPropertiesSet();
        reader.open(new ExecutionContext());
        System.out.println(reader.read());
    }

    @Test
    @DisplayName("전체 읽기")
    void test2() throws Exception {
        JdbcPagingItemReader<Chapter7_JDBC_Customer> reader = getReader("Juneau");
        reader.afterPropertiesSet();
        reader.open(new ExecutionContext());

        for (int i = 0; i < 4; i++) {
            System.out.println(reader.read());
        }
    }

    private JdbcPagingItemReader<Chapter7_JDBC_Customer> getReader(String city) throws Exception {
        PagingQueryProvider object = readerConfig.pagingQueryProvider(dataSource).getObject();
        return readerConfig.customerJdbcItemReader(dataSource, object, city);
    }

}