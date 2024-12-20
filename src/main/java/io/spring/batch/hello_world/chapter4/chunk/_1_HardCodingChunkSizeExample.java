package io.spring.batch.hello_world.chapter4.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class _1_HardCodingChunkSizeExample {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    // Reader Writer등은 7~9장 등에서 배울 내용이기 때문에 Job과 Step에 집중
    @Bean
    public Job hardCodingChunkSizeJob() {
        return new JobBuilder("hardCodingChunkSizeJob", jobRepository)
                .start(hardCodingChunkSizeStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step hardCodingChunkSizeStep() {
        return new StepBuilder("hardCodingChunkSizeStep", jobRepository)
                .<String, String>chunk(10, platformTransactionManager)
                .reader(itemReader(null))
                .writer(itemWriter(null))
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<String> itemReader(
            @Value("#{jobParameters['inputFile']}") Resource inputFile) {
        return new FlatFileItemReaderBuilder<String>()
                .name("inputFile")
                .resource(inputFile)
                .lineMapper(new PassThroughLineMapper())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<String> itemWriter(
            @Value("#{jobParameters['outputFile']}") Resource outputFile) {
        return new FlatFileItemWriterBuilder<String>()
                .name("itemWriter")
                .resource((WritableResource) outputFile)
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }
}
