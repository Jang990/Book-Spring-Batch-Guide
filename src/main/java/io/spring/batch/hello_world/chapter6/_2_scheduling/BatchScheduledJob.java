package io.spring.batch.hello_world.chapter6._2_scheduling;

import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduledJob extends QuartzJobBean {
    private final Job job;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    // 스케줄링된 이벤트가 발생할 때마다 한 번씩 호출
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        /*
            스프링 배치의 기본 잡 실행과 스케줄러의 잡 실행이 겹쳐서 데드락이 발생할 수 있다.
            이 예제를 예외없이 실행하려면 잠시 spring.batch.job.enabled를 false로 설정하자.
         */
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
