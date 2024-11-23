package io.spring.batch.hello_world.chapter4.stepflow._3_recycle;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class _2_FlowStep {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    // Flow를 마치 하나의 Step처럼 사용.
    // 그래서 Flow 내의 개별 스텝을 집계하지 않고도 플로우의 영향을 전체적으로 볼 수 있음.
    // 즉 Flow 전체의 모니터링과 리포팅의 이점이 있음
    @Bean
    public Step initializeBatch() {
        return new StepBuilder("initializeBatch", jobRepository)
                .flow(preProcessingFlow123())
                .build();
    }

    @Bean
    public Job conditionalStepLogicJob() {
        return new JobBuilder("flowStep", jobRepository)
                .start(initializeBatch())
                .next(runBatch4())
                .build();
    }

    @Bean
    public Flow preProcessingFlow123() {
        return new FlowBuilder<Flow>("preProcessingFlow")
                .start(loadFileStep1())
                .next(loadCustomerStep2())
                .next(updateStartStep3())
                .build();
    }


    @Bean
    public Step loadFileStep1() {
        return new StepBuilder("loadFileStep1", jobRepository)
                .tasklet(loadStockFile1(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step loadCustomerStep2() {
        return new StepBuilder("loadCustomerStep2", jobRepository)
                .tasklet(loadCustomerFile2(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step updateStartStep3() {
        return new StepBuilder("updateStartStep3", jobRepository)
                .tasklet(updateStart3(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step runBatch4() {
        return new StepBuilder("runBatch4", jobRepository)
                .tasklet(runBatchTasklet4(), platformTransactionManager)
                .build();
    }



    @Bean
    public Tasklet loadStockFile1() {
        return (contribution, chunkContext) -> {
            System.out.println("Stock 파일 로딩됨");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet loadCustomerFile2() {
        return (contribution, chunkContext) -> {
            System.out.println("Stock 파일 로딩됨");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet updateStart3() {
        return (contribution, chunkContext) -> {
            System.out.println("시작이 업데이트됨");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet runBatchTasklet4() {
        return (contribution, chunkContext) -> {
            System.out.println("플로우 스텝 배치 처리가 시작됨");
            return RepeatStatus.FINISHED;
        };
    }
}
