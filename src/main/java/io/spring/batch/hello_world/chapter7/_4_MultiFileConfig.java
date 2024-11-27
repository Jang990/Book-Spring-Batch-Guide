package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._3.CustomerFileReader;
import io.spring.batch.hello_world.chapter7._3.TransactionFieldSetMapper;
import io.spring.batch.hello_world.chapter7._4.CustomerFileReader2;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class _4_MultiFileConfig {

    // customerFile=chapter7/input/customerMultiFormat*
    // 이렇게 패턴으로 실행시키면 된다.
    @Bean
    @StepScope
    public MultiResourceItemReader multiResourceItemReader(
            @Value("#{jobParameter['customerFile']}") Resource[] inputFiles) {
        return new MultiResourceItemReaderBuilder<>()
                .name("multiResourceItemReader")
                .resources(inputFiles)
                .delegate(customerFileReader())
                .build();
    }

    @Bean
    @StepScope
    public CustomerFileReader2 customerFileReader() {
        return new CustomerFileReader2(customerItemReader());
    }

    @Bean
    @StepScope
    public FlatFileItemReader customerItemReader() {
        return new FlatFileItemReaderBuilder<Chapter7_Customer>()
                .name("customerItemReader")
                .lineMapper(lineTokenizer())
                .build();
    }

    @Bean
    public PatternMatchingCompositeLineMapper lineTokenizer() {
        Map<String, LineTokenizer> lineTokenizers = createCustomLineTokenizers();
        Map<String, FieldSetMapper> fieldSetMappers = createCustomFieldMappers();

        PatternMatchingCompositeLineMapper lineMappers = new PatternMatchingCompositeLineMapper();
        lineMappers.setTokenizers(lineTokenizers);
        lineMappers.setFieldSetMappers(fieldSetMappers);
        return lineMappers;
    }

    private Map<String, LineTokenizer> createCustomLineTokenizers() {
        Map<String, LineTokenizer> lineTokenizers = new HashMap<>(2);
        lineTokenizers.put("CUST*", customerLineTokenizer());
        lineTokenizers.put("TRANS*", transactionLineTokenizer());
        return lineTokenizers;
    }

    private Map<String, FieldSetMapper> createCustomFieldMappers() {
        Map<String, FieldSetMapper> fieldSetMappers = new HashMap<>(2);
        BeanWrapperFieldSetMapper<Chapter7_Customer> customerFieldSetMapper
                = new BeanWrapperFieldSetMapper<>();
        customerFieldSetMapper.setTargetType(Chapter7_Customer.class);
        fieldSetMappers.put("CUST*", customerFieldSetMapper);
        fieldSetMappers.put("TRANS*", new TransactionFieldSetMapper());
        return fieldSetMappers;
    }

    @Bean
    public DelimitedLineTokenizer transactionLineTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("prefix", "accountNumber", "transactionDate", "amount");
        return lineTokenizer;
    }

    @Bean
    public DelimitedLineTokenizer customerLineTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(
                "firstName", "middleInital", "lastName",
                "address", "city",
                "state", "zipCode"
        );
        lineTokenizer.setIncludedFields(1, 2, 3, 4, 5, 6, 7);
        return lineTokenizer;
    }
}
