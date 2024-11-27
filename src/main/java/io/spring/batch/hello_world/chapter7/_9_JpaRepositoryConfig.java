package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._9.Chapter7_CustomerRepository;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_JPA_Customer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;

import java.util.Collections;

public class _9_JpaRepositoryConfig {
    @Bean
    @StepScope
    public RepositoryItemReader<Chapter7_JPA_Customer> customerItemReader(
            Chapter7_CustomerRepository customerRepository,
            @Value("#{jobParameter['city']}") String city) {
        return new RepositoryItemReaderBuilder<Chapter7_JPA_Customer>()
                .name("customerItemReader")
                .arguments(Collections.singletonList(city))
                .methodName("findByCity")
                .repository(customerRepository)
                .sorts(Collections.singletonMap("lastName", Sort.Direction.ASC))
                .build();
    }
}
