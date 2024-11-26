package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._2.CustomerFileLineTokenizer;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class _2_DelimiterReaderConfig {

    // 예시 : Penelope,Y,Sandoval,2643,Fringilla Av.,College,AK,99557
    @Bean
    @StepScope
    public FlatFileItemReader<Chapter7_Customer> fixedFileItemReader(
            @Value("#{jobParameters['customerFile']}") Resource inputFile) {
        return new FlatFileItemReaderBuilder<Chapter7_Customer>()
                .name("customerItemReader")
                .resource(inputFile)
                // 1,2 둘 중 하나 사용
                // 1번. 파싱 방식 커스텀
                .lineTokenizer(new CustomerFileLineTokenizer())
                .targetType(Chapter7_Customer.class)

                // 2번. 매핑(=객체 생성) 방식 커스텀
                /*.delimited()
                .names(
                        "firstName", "middleInitial", "lastName",
                        "addressNumber", "street", "city",
                        "state", "zipCode"
                )
                .fieldSetMapper(new CustomerFiledSetMapper())*/
                .build();
    }
}
