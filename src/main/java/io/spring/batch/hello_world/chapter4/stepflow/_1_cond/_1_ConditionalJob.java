package io.spring.batch.hello_world.chapter4.stepflow._1_cond;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class _1_ConditionalJob {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Tasklet passTasklet() {
        return (contribution, chunkContext) -> {
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet exceptionTasklet() {
        return (contribution, chunkContext) -> {
            throw new RuntimeException("실패합니다!");
        };
    }

    @Bean
    public Tasklet successTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("성공!");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet failTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("실패!");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job job() {
        /*
        firstStep() -FAILED 이외-> successStep() --> 성공
                    \-  FAILED  -> failureStep() -/
         */
        return new JobBuilder("conditional_job", jobRepository)
                .start(firstStep()) // 결과: 성공!
//                .start(firstExceptionStep()) // 결과 : 실패합니다! 스택트레이스 + 실패!
                    .on("FAILED").to(failureStep()) // FAILED일 경우 실패 스탭으로 이동
                .from(firstStep()) // 첫 스텝으로 부터
                    .on("*").to(successStep()) // FAILED 외에 모든 경우 성공 스탭으로 이동
                .end()// Job 종료
                .incrementer(new RunIdIncrementer())
                .build();

    }

    @Bean
    public Step firstStep() {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(passTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step firstExceptionStep() {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(exceptionTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step successStep() {
        return new StepBuilder("secondStep", jobRepository)
                .tasklet(successTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step failureStep() {
        return new StepBuilder("failureStep", jobRepository)
                .tasklet(failTasklet(), platformTransactionManager)
                .build();
    }
}
