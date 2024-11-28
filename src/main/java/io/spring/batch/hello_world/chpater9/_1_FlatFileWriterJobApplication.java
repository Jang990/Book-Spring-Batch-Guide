package io.spring.batch.hello_world.chpater9;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.transaction.PlatformTransactionManager;

//@SpringBootApplication
@RequiredArgsConstructor
public class _1_FlatFileWriterJobApplication {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    @StepScope
    public FlatFileItemReader<Chapter9_Customer> fileReader(
            @Value("#{jobParameters['customerFile']}") Resource customerFile) {
        return new FlatFileItemReaderBuilder<Chapter9_Customer>()
                .name("fileReader")
                .resource(customerFile)
                .delimited()
                .names(
                        "firstName", "middleInitial", "lastName",
                        "address", "city", "state", "zip")
                .targetType(Chapter9_Customer.class)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Chapter9_Customer> fileWriter(
            @Value("#{jobParameters['outputFile']}") WritableResource outputFile) {
        return new FlatFileItemWriterBuilder<Chapter9_Customer>()
                .name("fileWriter")
                .resource(outputFile)
                .formatted()
                .format("%s %s lives at %s %s in %s, %s.")
                .names(
                        "firstName", "lastName", "address",
                        "city", "state", "zip")
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Chapter9_Customer> delimiterFileWriter(
            @Value("#{jobParameters['outputFile']}") WritableResource outputFile) {
        return new FlatFileItemWriterBuilder<Chapter9_Customer>()
                .name("fileWriter")
                .resource(outputFile)
                .delimited()
                .delimiter(";")
                .names(
                        "firstName", "lastName", "address",
                        "city", "state", "zip")
                .build();
    }

    @Bean
    public Step step() {
        return new StepBuilder("myStep", jobRepository)
                .<Chapter9_Customer, Chapter9_Customer>chunk(10, transactionManager)
                .reader(fileReader(null))
                .writer(delimiterFileWriter(null))
                .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("myJob", jobRepository)
                .start(step())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(_1_FlatFileWriterJobApplication.class,
                "customerFile=chapter9/input/customer.csv",
//                "outputFile=file:C:/Users/User/Desktop/formattedCustomers.csv"
                "outputFile=file:C:/Users/User/Desktop/delimiterCustomers.csv");
    }
}
