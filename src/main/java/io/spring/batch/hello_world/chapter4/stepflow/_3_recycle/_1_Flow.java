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
public class _1_Flow {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job conditionalStepLogicJob() {
        return new JobBuilder("conditionalStepLogicJob", jobRepository)
                .start(preProcessingFlow123())
                .next(runBatch4())
                .end().build();
    }

    // 그냥 Step의 뭉치로 Flow를 사용.
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
            System.out.println("배치 처리가 시작됨");
            return RepeatStatus.FINISHED;
        };
    }
}
