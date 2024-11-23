package io.spring.batch.hello_world.chapter5;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class _2_ExplorerJob {
    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Tasklet explorerTasklet() {
        return new ExploringTasklet(jobExplorer);
    }

    @Bean
    public Step explorerStep() {
        return new StepBuilder("explorerStep", jobRepository)
                .tasklet(explorerTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Job explorerJob() {
        return new JobBuilder("explorerJob", jobRepository)
                .start(explorerStep())
                .build();
    }
}
