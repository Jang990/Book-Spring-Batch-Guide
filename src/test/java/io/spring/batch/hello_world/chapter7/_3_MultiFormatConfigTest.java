package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class _3_MultiFormatConfigTest {
    _3_MultiFormatConfig readerConfig = new _3_MultiFormatConfig();
    private final String TARGET_PATH = "chapter7/input/customerMultiFormat.csv";

    @Test
    @DisplayName("파일을 읽어올 수 있는지 확인")
    void test1() throws Exception {
        Resource resource = new ClassPathResource(TARGET_PATH);
        FlatFileItemReader<Chapter7_Customer> customerReader = readerConfig.customerItemReader(resource);
        customerReader.open(new ExecutionContext());

        System.out.println(customerReader.read());
    }

    @Test
    @DisplayName("전체 읽기")
    void test2() throws Exception {
        Resource resource = new ClassPathResource(TARGET_PATH);
        FlatFileItemReader<Chapter7_Customer> customerReader = readerConfig.customerItemReader(resource);
        customerReader.open(new ExecutionContext());

        for (int i = 0; i < 9; i++) {
            System.out.println(customerReader.read());
        }
    }

}