package io.spring.batch.hello_world.chapter7;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;

//@Configuration
public class _6_JsonItemReaderConfig {

    @Bean
    @StepScope
    public JsonItemReader<Chapter7_Customer> customerFileReader(
            @Value("#{jobParameter['customerFile']}") Resource inputFile) {
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

        JacksonJsonObjectReader<Chapter7_Customer> jsonObjectReader =
                new JacksonJsonObjectReader<>(Chapter7_Customer.class);
        jsonObjectReader.setMapper(om);

        return new JsonItemReaderBuilder<Chapter7_Customer>()
                .name("customerFileReader")
                .jsonObjectReader(jsonObjectReader)
                .resource(inputFile)
                .build();
    }
}
