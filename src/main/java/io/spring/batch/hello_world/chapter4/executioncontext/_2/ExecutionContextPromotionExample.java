package io.spring.batch.hello_world.chapter4.executioncontext._2;

import io.spring.batch.hello_world.chapter4.DailyJobTimeStamper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ExecutionContextPromotionExample {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
                .start(step1())
                .next(step2())
                .incrementer(new DailyJobTimeStamper())
                .validator(jobParametersValidator())
                .build();
    }

    @Bean
    public JobParametersValidator jobParametersValidator() {
        return new DefaultJobParametersValidator(
                new String[]{"name"},
                new String[]{"currentDate"}
        );
    }

    /** StepExecutionContext에 이름을 멋지게 꾸민 processedName 저장 */
    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new HelloWorldTasklet(), platformTransactionManager)
                .listener(promotionListener())
                .build();
    }

    /**
     * 동적 생성된 StepExecutionContext의 키를 JobExecutionContext로 승격
     *
     * 하나의 Step에서 동적으로 생성된 데이터를 다른 Step에서도 사용해야 할 때 사용
     * ex) 복잡한 연산 결과 공유, 동적인 데이터 생성, 데이터 추적 및 로깅 등등
     */
    @Bean
    public StepExecutionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{"processedName"}); // 승격할 StepExecutionContext 키
        return listener;
    }

    /** step1에서 승격된 키를 활용 가능 */
    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new GoodByeTasklet(), platformTransactionManager)
                .build();
    }
}
