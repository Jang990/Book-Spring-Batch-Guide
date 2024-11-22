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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class _3_CustomChunkSizePolicyExample {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    /*
    ...
    현재 아이템 = c0d7287d-5a85-4b38-880f-61ab8997b808
     랜덤으로 선정된 청크 사이즈 = 3
    현재 아이템 = ea174fb5-c869-403b-8e40-2475a41a5caf
    현재 아이템 = 4c878be8-7f5c-4d23-bb5c-194a4d87ac5f
    현재 아이템 = 910a6b9e-edb8-4cd2-92cf-0174e20c6adf
     랜덤으로 선정된 청크 사이즈 = 2
    현재 아이템 = 46d29b81-08fd-4007-bd1c-633d5eaad757
    현재 아이템 = 58175dc6-145f-4732-9ee0-c36e09c751ce
     랜덤으로 선정된 청크 사이즈 = 9
    현재 아이템 = 11e6d34b-157e-4d84-9bec-e2c8424d5e32
    현재 아이템 = 54b24536-07de-415e-aa8f-207b898f31d8
    ...
     */
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
