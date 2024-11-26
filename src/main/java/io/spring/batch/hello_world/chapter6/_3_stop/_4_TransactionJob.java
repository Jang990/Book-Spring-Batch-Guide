package io.spring.batch.hello_world.chapter6._3_stop;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class _4_TransactionJob {
    private final JobRepository jobRepository;

    // 파라미터
    // transactionFile=chapter6/transactionFile.csv summaryFile=file:C:\\Users\\User\\Desktop\\summaryFile1.csv
    // 주의 : jar을 실행시키는 방식이라면 transactionFile의 마지막 줄을 변경할 때마다 새로 빌드해줘야함.
    @Bean
    public Job transactionJob(
            Step importTransactionFileStep,
            Step applyTransactionsStep,
            Step generatedAccountSummaryStep) {
        return new JobBuilder("transactionJob", jobRepository)
//                .preventRestart() // 중지, 실패해도 같은 파라미터로 다시 시작 못하게 설정.
                .incrementer(new RunIdIncrementer())
                .start(importTransactionFileStep)
                    .on("STOPPED").stopAndRestart(importTransactionFileStep)
                .from(importTransactionFileStep)
                    .on("*").to(applyTransactionsStep)
                .from(applyTransactionsStep)
                    .next(generatedAccountSummaryStep)
                .end().build();
    }
}
