package io.spring.batch.hello_world.chapter6._2_scheduling;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail quartzZobDetail() {
        return JobBuilder.newJob(BatchScheduledJob.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5).withRepeatCount(4);
        // 5초마다. 4번 반복(총 5번)

        return TriggerBuilder.newTrigger()
                .forJob(quartzZobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }
}
