package io.spring.batch.hello_world.chapter4;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class HelloWorldJob {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job job(Step step, CompositeJobParametersValidator compositeValidator) {
        return new JobBuilder("basicJob", jobRepository)
                .start(step)
                .validator(compositeValidator)
                .build();
    }

    @Bean
    public Step step(Tasklet tasklet) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(tasklet, platformTransactionManager)
                .build();
    }

    @StepScope /* 스텝의 실행 범위에 들어갈 때 까지 빈 생성 지연시켜서 name을 받을 수 있게 만든다.
                    이렇게 뒤로 밀어서 받아들인 잡 파라미터를 빈 생성 시점에 주입할 수 있다.*/
    @Bean
    public Tasklet helloWorldTasklet(
            @Value("#{jobParameters['name']}") String name,
            @Value("#{jobParameters['fileName']}") String fileName) {
        return (contribution, chunkContext) -> {
            System.out.println("Hello, %s!".formatted(name));
            System.out.println("fileName =  %s!".formatted(fileName));
            return RepeatStatus.FINISHED;
        };
    }
}
