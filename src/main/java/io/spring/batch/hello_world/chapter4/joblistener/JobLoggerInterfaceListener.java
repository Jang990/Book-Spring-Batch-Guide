package io.spring.batch.hello_world.chapter4.joblistener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobLoggerInterfaceListener implements JobExecutionListener {
    private static String START_MESSAGE = " ====> InterfaceListener: %s is beginning execution";
    private static String END_MESSAGE = " ====> InterfaceListener: %s has completed with the status %s";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(
                START_MESSAGE.formatted(jobExecution.getJobInstance().getJobName())
        );
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(
                END_MESSAGE.formatted(
                        jobExecution.getJobInstance().getJobName(),
                        jobExecution.getStatus()
                )
        );
    }
}
