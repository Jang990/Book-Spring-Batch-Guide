package io.spring.batch.hello_world.chapter6._3_stop;

import io.spring.batch.hello_world.chapter6._3_stop.domain.AccountSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class _3_GeneratedAccountSummaryStep {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    @StepScope
    public FlatFileItemWriter<AccountSummary> accountSummaryFileWriter(
            @Value("#{jobParameters['summaryFile']}") WritableResource resource) {
        DelimitedLineAggregator<AccountSummary> lineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor fieldExtractor = new BeanWrapperFieldExtractor();
        fieldExtractor.setNames(new String[] {"accountNumber", "currentBalance"});
        fieldExtractor.afterPropertiesSet();
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<AccountSummary>()
                .name("accountSummaryFileWriter")
                .resource(resource)
                .lineAggregator(lineAggregator)
                .build();
    }

    @Bean
    public Step generatedAccountSummaryStep(JdbcCursorItemReader<AccountSummary> accountSummaryReader) {
        return new StepBuilder("generatedAccountSummaryStep", jobRepository)
                .<AccountSummary, AccountSummary>chunk(100, transactionManager)
                .reader(accountSummaryReader)
                .writer(accountSummaryFileWriter(null))
                .build();
    }
}
