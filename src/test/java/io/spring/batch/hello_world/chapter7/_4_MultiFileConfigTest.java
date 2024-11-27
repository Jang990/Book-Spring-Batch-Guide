package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import static org.junit.jupiter.api.Assertions.*;

class _4_MultiFileConfigTest {


    _4_MultiFileConfig readerConfig = new _4_MultiFileConfig();
    private final String TARGET_PATH = "chapter7/input/customerMultiFormat*";
    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Test
    @DisplayName("파일을 읽어올 수 있는지 확인")
    void test1() throws Exception {
        Resource[] resources = resolver.getResources(TARGET_PATH);
        MultiResourceItemReader customerReader = readerConfig.multiResourceItemReader(resources);
        customerReader.open(new ExecutionContext());

        System.out.println(customerReader.read());
    }

    @Test
    @DisplayName("전체 읽기")
    void test2() throws Exception {
        Resource[] resources = resolver.getResources(TARGET_PATH);
        MultiResourceItemReader customerReader = readerConfig.multiResourceItemReader(resources);
        customerReader.open(new ExecutionContext());

        for (int i = 0; i < 12; i++) {
            Chapter7_Customer result = (Chapter7_Customer) customerReader.read();
            System.out.println(result);
            System.out.println(result.getTransactions().size());
        }
    }

}