package io.spring.batch.hello_world.chapter6.web;

import lombok.Data;

import java.util.Map;

@Data
public class JobLaunchRequest {
    private String name;
    private Map<String, String> jobParameters;
}
