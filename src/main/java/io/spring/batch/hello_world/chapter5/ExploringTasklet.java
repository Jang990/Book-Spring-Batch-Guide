package io.spring.batch.hello_world.chapter5;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

@RequiredArgsConstructor
public class ExploringTasklet implements Tasklet {
    private final JobExplorer jobExplorer;

    /*
    explorerJob 잡에는 3개의 인스턴스가 있습니다.
    **************************
        ==77 인스턴스는 1개의 Execution을 가지고 있습니다.
                Execution 111의 ExitStatus => exitCode=UNKNOWN;exitDescription=
        ==76 인스턴스는 1개의 Execution을 가지고 있습니다.
                Execution 110의 ExitStatus => exitCode=COMPLETED;exitDescription=
        ==75 인스턴스는 1개의 Execution을 가지고 있습니다.
                Execution 109의 ExitStatus => exitCode=COMPLETED;exitDescription=
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String jobName = chunkContext
                .getStepContext()
                .getJobName();

        List<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, Integer.MAX_VALUE);

        System.out.println("%s 잡에는 %d개의 인스턴스가 있습니다.".formatted(jobName, instances.size()));
        System.out.println("**************************");

        for (JobInstance instance : instances) {
            List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(instance);
            System.out.println(
                    "==%d 인스턴스는 %d개의 Execution을 가지고 있습니다.".formatted(
                            instance.getInstanceId(), jobExecutions.size()
                    )
            );

            for (JobExecution jobExecution : jobExecutions) {
                System.out.println(
                        "\tExecution %d의 ExitStatus => %s".formatted(
                                jobExecution.getId(), jobExecution.getExitStatus()
                        )
                );
            }
        }

        return RepeatStatus.FINISHED;
    }
}
