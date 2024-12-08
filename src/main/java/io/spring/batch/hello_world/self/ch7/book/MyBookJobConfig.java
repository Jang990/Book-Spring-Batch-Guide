package io.spring.batch.hello_world.self.ch7.book;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class MyBookJobConfig {

    @Bean
    public ItemWriter<MyBook> itemWriter() {
        return System.out::println;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<MyBook> myBookCsvFileReader(Resource resource) {
        return new FlatFileItemReaderBuilder<MyBook>()
                .name("myBookCsvFileReader")
                .resource(resource)
                .encoding("euc-kr")
                .lineTokenizer(new CustomMyBookDelimiterTokenizer())
                .fieldSetMapper(new CustomMyBookMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public Step myBookStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<MyBook> myBookCsvFileReader) {
        return new StepBuilder("myStep", jobRepository)
                .<MyBook, MyBook>chunk(10, transactionManager)
                .reader(myBookCsvFileReader)
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job myJob(JobRepository jobRepository, Step myBookStep) {
        return new JobBuilder("myJob", jobRepository)
                .start(myBookStep)
                .build();
    }

}
