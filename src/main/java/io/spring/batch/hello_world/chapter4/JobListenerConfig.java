package io.spring.batch.hello_world.chapter4;

import io.spring.batch.hello_world.chapter4.joblistener.JobLoggerAnnotationListener;
import io.spring.batch.hello_world.chapter4.joblistener.JobLoggerInterfaceListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobListenerConfig {
    @Bean
    public JobLoggerInterfaceListener jobLoggerInterfaceListener() {
        return new JobLoggerInterfaceListener();
    }

    @Bean
    public JobLoggerAnnotationListener jobLoggerAnnotationListener() {
        return new JobLoggerAnnotationListener();
    }
}
