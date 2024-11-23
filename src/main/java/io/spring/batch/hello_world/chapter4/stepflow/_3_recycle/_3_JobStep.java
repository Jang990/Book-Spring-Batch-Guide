package io.spring.batch.hello_world.chapter4.stepflow._3_recycle;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class _3_JobStep {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    // 잡스텝 - 개별 잡을 상위 잡과 묶는 강력한 기능
    // 그러나 개별 스텝의 실행 제어에 매우 큰 제약이 있다.
    // 마스터잡이 잡스텝->잡스텝->잡스텝이라면 잡스텝안의 별개의 잡들을 제어하기 힘들기 때문에 피하는게 좋다.
    @Bean
    public Step initializeBatch() {
        return new StepBuilder("initializeBatch", jobRepository)
                .job(preProcessingJob123())
                .parametersExtractor(new DefaultJobParametersExtractor())
                .build();
    }

    @Bean
    public Job jobStep() {
        return new JobBuilder("jobStep", jobRepository)
                .start(initializeBatch())
                .next(runBatch4())
                .build();
    }

    // @Bean을 쓰고 싶다면 프로퍼티를 spring.batch.job.names=jobStep 으로 구성해야 한다.
//    @Bean
    public Job preProcessingJob123() {
        return new JobBuilder("preProcessingJob", jobRepository)
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
            System.out.println("잡스텝 배치 처리가 시작됨");
            return RepeatStatus.FINISHED;
        };
    }
}
