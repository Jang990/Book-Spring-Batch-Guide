package io.spring.batch.hello_world.chapter4.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Configuration
@RequiredArgsConstructor
public class _2_CompositeChunkPolicyExample {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job compositeChunkPolicyJob() {
        return new JobBuilder("compositeChunkPolicyJob", jobRepository)
                .start(compositeChunkPolicyStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step compositeChunkPolicyStep() {
        return new StepBuilder("compositeChunkPolicyStep", jobRepository)
                .<String, String>chunk(10, platformTransactionManager)
                .reader(itemReader())
                .writer(itemWriter())
                .transactionManager(platformTransactionManager)
                .build();
    }

    // 100_000개 읽기 딱 1번
    @Bean
    public ItemReader<String> itemReader() {
        int initialCapacity = 100_000;
        List<String> items = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            items.add(UUID.randomUUID().toString());
        }

        return new ListItemReader<>(items);
    }

    // 100_000개의 아이템 쓰기
    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            for (String item : items) {
                System.out.println("==> 현재 아이템 = " + item);
            }
        };
    }

    @Bean
    public CompletionPolicy completionPolicy() {
        CompositeCompletionPolicy policy = new CompositeCompletionPolicy();

        policy.setPolicies(
                new CompletionPolicy[] {
                        new TimeoutTerminationPolicy(3), // 3ms이 지나면 해당 청크 완료로 간주하고 계속
                        new SimpleCompletionPolicy(1000) // 1000개를 처리하면 완료 간주하고 계속
                }
        );

        return policy;
    }
}
