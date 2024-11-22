package io.spring.batch.hello_world.chapter4.stepflow._1_cond;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Random;

public class RandomDecider implements JobExecutionDecider {
    private Random random = new Random();

    // 랜덤 성공 실패
    @Override
    public FlowExecutionStatus decide(
            JobExecution jobExecution, // Job 세션. = 웹 세션과 유사
            StepExecution stepExecution) { // 현재 Step 세션
        if(random.nextBoolean())
            return new FlowExecutionStatus(FlowExecutionStatus.COMPLETED.getName());
        return new FlowExecutionStatus(FlowExecutionStatus.FAILED.getName());
    }
}
