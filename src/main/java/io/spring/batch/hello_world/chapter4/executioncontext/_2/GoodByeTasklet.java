package io.spring.batch.hello_world.chapter4.executioncontext._2;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class GoodByeTasklet implements Tasklet {
    private static final String GOODBYE_WORLD = " ===> GoobBye, %s";
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String processedName = contribution.getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .getString("processedName");

        System.out.println(GOODBYE_WORLD.formatted(processedName));

        return RepeatStatus.FINISHED;
    }
}
