package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_JPA_Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public ItemWriter<Chapter7_JPA_Customer> itemWriter() {
        return (items) -> items.forEach(System.out::println);
    }

    @Bean
    public Step copyFileStep(RepositoryItemReader<Chapter7_JPA_Customer> jpaRepositoryFindAllReader) {
        return new StepBuilder("copyFileStep", jobRepository)
                .<Chapter7_JPA_Customer, Chapter7_JPA_Customer>chunk(10, transactionManager)
                .reader(jpaRepositoryFindAllReader)
                .writer(itemWriter())
                .allowStartIfComplete(true)

                .faultTolerant()
//                    .skip(Exception.class).skipLimit(10) // 이건 10번까지만 스킵해.
                    .noSkip(ParseException.class) // 이건 스킵하지말고 실패처리해.

                .build();
    }

    @Bean
    public Job job(RepositoryItemReader<Chapter7_JPA_Customer> jpaRepositoryFindAllReader) {
        return new JobBuilder("job", jobRepository)
                .start(copyFileStep(jpaRepositoryFindAllReader))
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
