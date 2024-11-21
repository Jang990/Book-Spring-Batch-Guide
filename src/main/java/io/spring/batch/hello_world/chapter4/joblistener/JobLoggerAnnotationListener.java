package io.spring.batch.hello_world.chapter4.joblistener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class JobLoggerAnnotationListener {
    private static String START_MESSAGE = " ====> AnnotationListener: %s is beginning execution";
    private static String END_MESSAGE = " ====> AnnotationListener: %s has completed with the status %s";

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(
                START_MESSAGE.formatted(jobExecution.getJobInstance().getJobName())
        );
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.println(
                END_MESSAGE.formatted(
                        jobExecution.getJobInstance().getJobName(),
                        jobExecution.getStatus()
                )
        );
    }
}
