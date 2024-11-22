package io.spring.batch.hello_world.chapter4.steplistener;


import io.spring.batch.hello_world.chapter4.chunk.RandomChunkSizePolicy;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class _1_ChunkJob {
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
                .<String, String>chunk(customPolicy(), platformTransactionManager)
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new LoggingStepStartStopListener()) // 청크도 가능하다는 것 알아두기 | @BeforeChunk @AfterChunk
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public ItemReader<String> itemReader() {
        int initialCapacity = 100;
        List<String> items = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            items.add(UUID.randomUUID().toString());
        }

        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            for (String item : items) {
                System.out.println("==> 현재 아이템 = " + item);
            }
        };
    }

    @Bean
    public CompletionPolicy customPolicy() {
        return new RandomChunkSizePolicy();
    }
}
