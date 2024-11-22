package io.spring.batch.hello_world.chapter4.taskletimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class LambdaTaskletImpl_1 {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job lambdaJob() {
        return new JobBuilder("LambdaTask_Job", jobRepository)
                .start(lambdaStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step lambdaStep() {
        return new StepBuilder("LambdaTask_step1", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("===> Tasklet Lambda Impl");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }
}
