package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._9.Chapter7_CustomerRepository;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_JPA_Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class _9_JpaRepositoryConfigTest {
    @Autowired
    Chapter7_CustomerRepository repository;

    _9_JpaRepositoryConfig readerConfig = new _9_JpaRepositoryConfig();

    @Test
    @DisplayName("DB에서 읽어올 수 있는지 확인")
    void test1() throws Exception {
        RepositoryItemReader<Chapter7_JPA_Customer> reader = readerConfig.jpaRepositoryReader(repository, "Juneau");
        reader.open(new ExecutionContext());
        System.out.println(reader.read());
    }

}