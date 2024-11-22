package io.spring.batch.hello_world.chapter4.steplistener;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class LoggingStepStartStopListener {
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        System.out.println(stepExecution.getStepName() + "가 시작됨!");
    }

    @AfterStep
    public void afterStep(StepExecution stepExecution) {
        System.out.println(stepExecution.getStepName() + "가 끝남!");
    }
}
