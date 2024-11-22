package io.spring.batch.hello_world.chapter4.taskletimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class MethodInvokingTaskletImpl_3 {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    // 메소드가 ExitStatus 타입을 반환하지 않는 한 결괏값으로 COMPLETED를 반환한다.
    @Bean
    public Job methodJob() {
        return new JobBuilder("LambdaTask_Job", jobRepository)
                .start(methodStep())
                .next(methodStepWithParam())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step methodStep() {
        return new StepBuilder("LambdaTask_step1", jobRepository)
                .tasklet(methodInvokingTasklet1(), platformTransactionManager)
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter methodInvokingTasklet1() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(myCustomService());
        adapter.setTargetMethod("printConsole");

        return adapter;
    }

    @Bean
    public Step methodStepWithParam() {
        return new StepBuilder("LambdaTask_step1", jobRepository)
                .tasklet(methodInvokingTaskletWithParam(null), platformTransactionManager)
                .build();
    }

    @StepScope
    @Bean
    public MethodInvokingTaskletAdapter methodInvokingTaskletWithParam(
            @Value("#{jobParameters['message']}") String message) {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(myCustomService());
        adapter.setTargetMethod("printConsole");
        adapter.setArguments(new String[]{message});

        return adapter;
    }

    @Bean
    public MyMethodService myCustomService() {
        return new MyMethodService();
    }
}
