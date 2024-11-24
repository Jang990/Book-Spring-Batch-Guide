package io.spring.batch.hello_world.chapter6._3_stop;

import io.spring.batch.hello_world.chapter6._3_stop.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class _1_ImportTransactionFileStepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    // 거래 기록 파일을 읽어와서 DB에 저장.
    @Bean
    public Step importTransactionFileStep() {
        return new StepBuilder("importTransactionFileStep", jobRepository)
                .<Transaction, Transaction>chunk(100, transactionManager)
                .reader(transactionReader()) // 거래 기록 읽어오기
                .writer(transactionWriter(dataSource)) // 읽은 거래 기록 저장하기
                .allowStartIfComplete(true)
                .listener(transactionReader()) // TransactionReader의 AfterStep
                .build();
    }

    // 커스텀 유효성 검사를 위헤 FlatFileItemReader를 래핑해서 사용
    @Bean
    @StepScope
    public TransactionReader transactionReader() {
        return new TransactionReader(fileItemReader(null));
    }

    // 파일을 읽어오는 Reader
    // 7장에서 자세히 다룸.
    @Bean
    @StepScope
    public FlatFileItemReader<FieldSet> fileItemReader(
            @Value("#{jobParameters['transactionFile']}")Resource inputFile) {
        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("fileItemReader")
                .resource(inputFile)
                .lineTokenizer(new DelimitedLineTokenizer())
                .fieldSetMapper(new PassThroughFieldSetMapper())
                .build();
    }

    // 읽어온 거래 기록을 DB에 저장
    // 9장에서 자세히 다룸. 기록한다고만 알아두기.
    @Bean
    public JdbcBatchItemWriter<Transaction> transactionWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .itemSqlParameterSourceProvider(
                        new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("""
                        INSERT INTO TRANSACTION (ACCOUNT_SUMMARY_ID, TIMESTAMP, AMOUNT)
                        VALUES
                        (
                            (SELECT ID FROM ACCOUNT_SUMMARY WHERE ACCOUNT_NUMBER = :accountNumber),
                            :timestamp,
                            :accountNumber
                        )
                        """)
                .dataSource(dataSource)
                .build();
    }
}
