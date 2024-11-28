package io.spring.batch.hello_world.chpater9;

import io.spring.batch.hello_world.chpater9._2.CustomerItemPreparedStatementSetter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication
@RequiredArgsConstructor
public class _2_DBWriterJobApplication {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

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
    public JdbcBatchItemWriter<Chapter9_Customer> jdbcWriter() {
        return new JdbcBatchItemWriterBuilder<Chapter9_Customer>()
                .dataSource(dataSource)
                // 둘 중 하나 선택하기.
                /*.sql("""
                        INSERT INTO CH9_CUSTOMER
                        (FIRST_NAME, MIDDLE_INITIAL, LAST_NAME, ADDRESS, CITY, STATE, ZIP)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """)
                .itemPreparedStatementSetter(
                        new CustomerItemPreparedStatementSetter())*/

                .sql("""
                        INSERT INTO CH9_CUSTOMER
                        (FIRST_NAME, MIDDLE_INITIAL, LAST_NAME, ADDRESS, CITY, STATE, ZIP)
                        VALUES (
                            :firstName, :middleInitial, :lastName,
                            :address, :city, :state, :zip
                        )
                        """)
                .beanMapped()

                .build();
    }

    @Bean
    public Step step() {
        return new StepBuilder("myStep", jobRepository)
                .<Chapter9_Customer, Chapter9_Customer>chunk(10, transactionManager)
                .reader(fileReader(null))
                .writer(jdbcWriter())
                .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("myJob", jobRepository)
                .start(step())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(_2_DBWriterJobApplication.class,
                "customerFile=chapter9/input/customer.csv", "name=2s");
    }
}
