package io.spring.batch.hello_world.chapter4.executioncontext._2;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class HelloWorldTasklet implements Tasklet {
    private static final String HELLO_WORLD = " ===> Hello, %s";

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String name = (String) chunkContext.getStepContext()
                .getJobParameters()
                .get("name");

        String processedResult = "정말 멋진 === " + name + " === 이름 레이아웃 ";

        chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext()
                .put("processedName", processedResult);

        System.out.println(HELLO_WORLD.formatted(processedResult));
        return RepeatStatus.FINISHED;
    }
}
