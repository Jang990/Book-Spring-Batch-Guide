package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._1._1_Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@RequiredArgsConstructor
public class _1_FixedFileReaderConfig {

    // 예시 : Penelope   YSandoval  2643Fringilla Av.       College         AK99557
    @Bean
    @StepScope
    public FlatFileItemReader<_1_Customer> fixedFileItemReader(
            @Value("#{jobParameters['customerFile']}") Resource inputFile) {
        return new FlatFileItemReaderBuilder<_1_Customer>()
                .name("customerItemReader")
                .resource(inputFile)
                .fixedLength()
                .columns(
                        new Range(1, 11), new Range(12, 12), new Range(13, 22),
                        new Range(23, 26), new Range(27, 46), new Range(47, 62),
                        new Range(63, 64), new Range(65, 69)
                )
                .names(
                        "firstName", "middleInitial", "lastName",
                        "addressNumber", "street", "city",
                        "state", "zipCode"
                )
                .targetType(_1_Customer.class)
                .build();
    }
}
