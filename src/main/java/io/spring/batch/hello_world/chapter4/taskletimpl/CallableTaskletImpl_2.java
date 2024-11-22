package io.spring.batch.hello_world.chapter4.taskletimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.Callable;

//@Configuration
@RequiredArgsConstructor
public class CallableTaskletImpl_2 {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job callableJob() {
        return new JobBuilder("CallableTask_Job", jobRepository)
                .start(callableStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step callableStep() {
        return new StepBuilder("CallableTask_Step1", jobRepository)
                .tasklet(callableTaskletAdapter(), platformTransactionManager)
                .build();
    }

    @Bean
    public CallableTaskletAdapter callableTaskletAdapter() {
        CallableTaskletAdapter adapter = new CallableTaskletAdapter();
        adapter.setCallable(callableObject());
        return adapter;
    }

    @Bean // java.util.concurrent | 반환 및 체크 예외를 밖으로 던질 수 있는 Runnable이라 생각하면 쉬움
    public Callable<RepeatStatus> callableObject() {
        return () -> {
            System.out.println("===> Tasklet Callable Impl");
            return RepeatStatus.FINISHED;
        };
    }
}
