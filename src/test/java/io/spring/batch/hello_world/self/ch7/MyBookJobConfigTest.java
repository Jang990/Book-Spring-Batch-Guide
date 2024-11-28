package io.spring.batch.hello_world.self.ch7;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

class MyBookJobConfigTest {
    MyBookJobConfig config = new MyBookJobConfig();

    private final String TARGET_PATH = "sample/가슴따뜻한작은도서관 장서 대출목록 (2024년 09월).csv";
    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Test
    @DisplayName("파일 리딩 가능")
    void test1() throws Exception {
        Resource resource = resolver.getResource(TARGET_PATH);
        FlatFileItemReader<MyBook> reader = config.myBookCsvFileReader(resource);
        reader.open(new ExecutionContext());

        MyBook result = null;
        int idx = 0;
        while ((result = reader.read()) != null) {
            System.out.println(idx+ " " + result);
            idx++;
        }
    }


}