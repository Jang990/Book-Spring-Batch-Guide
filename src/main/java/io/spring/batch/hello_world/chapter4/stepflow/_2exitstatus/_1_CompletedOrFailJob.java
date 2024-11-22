package io.spring.batch.hello_world.chapter4.stepflow._2exitstatus;

import io.spring.batch.hello_world.chapter4.stepflow._1_cond.RandomDecider;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class _1_CompletedOrFailJob {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Tasklet passTasklet() {
        return (contribution, chunkContext) -> {
            return RepeatStatus.FINISHED;
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
                    \-  FAILED  -> 실패
         */
        return new JobBuilder("conditional_job", jobRepository)
                .start(firstStep())
                .next(decider())
                    .on("FAILED").fail()
                .from(decider())
                    .on("*").to(successStep()).end()
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
    public Step successStep() {
        return new StepBuilder("secondStep", jobRepository)
                .tasklet(successTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new RandomDecider();
    }
}
