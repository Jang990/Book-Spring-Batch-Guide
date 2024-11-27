package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_JPA_Customer;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class _8_JpaConfigTest {
    @Autowired
    EntityManagerFactory emf;

    _8_JpaConfig readerConfig = new _8_JpaConfig();

    @Test
    @DisplayName("DB에서 읽어올 수 있는지 확인")
    void test1() throws Exception {
        JpaPagingItemReader<Chapter7_JPA_Customer> reader = readerConfig.customerItemReader(emf, "Juneau");
        reader.open(new ExecutionContext());
        System.out.println(reader.read());
    }

}