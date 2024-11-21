package io.spring.batch.hello_world.chapter4.executioncontext._1;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class ExecutionContextUsageExample implements Tasklet {
    private static final String HELLO_WORLD = "Hello, %s";

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String name = (String) chunkContext.getStepContext()
                .getJobParameters()
                .get("name");

        // job의 ExecutionContext 사용법
        ExecutionContext jobContext = chunkContext.getStepContext()
                .getStepExecution() // 20~22 라인 코드대신 .getJobExecutionContext()를 써도 된다. 편의 메서드다.
                .getJobExecution()
                .getExecutionContext();

        // step의 ExecutionContext 사용법
        /*ExecutionContext stepContext = chunkContext.getStepContext()
                .getStepExecution() // .getStepExecutionContext() 마찬가지로 편의 메서드
                .getExecutionContext();*/
        jobContext.put("user.name", name);

        System.out.println(HELLO_WORLD.formatted(name));
        return RepeatStatus.FINISHED;
    }
}
