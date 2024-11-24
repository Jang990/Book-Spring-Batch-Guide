package io.spring.batch.hello_world.chapter6._3_stop;

import io.spring.batch.hello_world.chapter6._3_stop.dao.TransactionDao;
import io.spring.batch.hello_world.chapter6._3_stop.dao.TransactionDaoSupport;
import io.spring.batch.hello_world.chapter6._3_stop.domain.AccountSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class _2_ApplyTransactionsStep {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    // 1. 거래 정보가 있는 계좌정보 가져오기.
    @Bean
    @StepScope
    public JdbcCursorItemReader<AccountSummary> accountSummaryReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<AccountSummary>()
                .name("accountSummaryReader")
                .dataSource(dataSource)
                .sql("""
                        SELECT ACCOUNT_NUMBER, CURRENT_BALANCE
                        FROM ACCOUNT_SUMMARY A
                        WHERE A.ID IN (
                            SELECT DISTINCT T.ACCOUNT_SUMMARY_ID
                            FROM TRANSACTION T
                        )
                        ORDER BY A.ACCOUNT_NUMBER
                        """)
                .rowMapper((resultSet, rowNumber) -> {
                    AccountSummary summary = new AccountSummary();
                    summary.setAccountNumber(resultSet.getString("account_number"));
                    summary.setCurrentBalance(resultSet.getDouble("current_balance"));
                    return summary;
                })
                .build();
    }

    @Bean
    public TransactionDao transactionDao(DataSource dataSource) {
        return new TransactionDaoSupport(dataSource);
    }

    // 2. 거래 정보에 기반해 계좌별 잔액 계산
    @Bean
    public TransactionApplierProcessor transactionApplierProcessor() {
        return new TransactionApplierProcessor(transactionDao(null));
    }

    // 3. 계산한 잔액으로 DB의 계좌 잔액 업데이트
    @Bean
    public JdbcBatchItemWriter<AccountSummary> accountSummaryWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AccountSummary>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(
                        new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("""
                        UPDATE ACCOUNT_SUMMARY
                        SET CURRENT_BALANCE = :currentBalance
                        WHERE ACCOUNT_NUMBER = :accountNumber
                        """)
                .build();
    }

    @Bean
    public Step applyTransactionsStep() {
        return new StepBuilder("applyTransactionsStep", jobRepository)
                .<AccountSummary, AccountSummary>chunk(100, transactionManager)
                .reader(accountSummaryReader(dataSource))
                .processor(transactionApplierProcessor())
                .writer(accountSummaryWriter(dataSource))
                .build();
    }
}
