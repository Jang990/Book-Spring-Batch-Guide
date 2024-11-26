package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._1._1_Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public ItemWriter<_1_Customer> itemWriter() {
        return (items) -> items.forEach(System.out::println);
    }

    @Bean
    public Step copyFileStep(FlatFileItemReader<_1_Customer> fixedFileItemReader) {
        return new StepBuilder("copyFileStep", jobRepository)
                .<_1_Customer, _1_Customer>chunk(10, transactionManager)
                .reader(fixedFileItemReader)
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job job(FlatFileItemReader<_1_Customer> fixedFileItemReader) {
        return new JobBuilder("job", jobRepository)
                .start(copyFileStep(fixedFileItemReader))
                .build();
    }
}
