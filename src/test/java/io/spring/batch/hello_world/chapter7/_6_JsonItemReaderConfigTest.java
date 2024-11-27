package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

class _6_JsonItemReaderConfigTest {
    _6_JsonItemReaderConfig readerConfig = new _6_JsonItemReaderConfig();
    private final String TARGET_PATH = "chapter7/input/customer.json";

    @Test
    @DisplayName("파일을 읽어올 수 있는지 확인")
    void test1() throws Exception {
        Resource resource = new ClassPathResource(TARGET_PATH);
        JsonItemReader<Chapter7_Customer> customerReader = readerConfig.customerFileReader(resource);
        customerReader.open(new ExecutionContext());

        System.out.println(customerReader.read());
    }

    @Test
    @DisplayName("전체 읽기")
    void test2() throws Exception {
        Resource resource = new ClassPathResource(TARGET_PATH);
        JsonItemReader<Chapter7_Customer> customerReader = readerConfig.customerFileReader(resource);
        customerReader.open(new ExecutionContext());

        for (int i = 0; i < 2; i++) {
            System.out.println(customerReader.read());
        }
    }

}