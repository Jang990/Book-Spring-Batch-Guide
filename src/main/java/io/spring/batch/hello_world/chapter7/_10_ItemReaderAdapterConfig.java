package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._10.CustomerService;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;

public class _10_ItemReaderAdapterConfig {
    @Bean
    public ItemReaderAdapter<Chapter7_Customer> customerItemReader(CustomerService customerService) {
        ItemReaderAdapter<Chapter7_Customer> adapter = new ItemReaderAdapter<>();
        adapter.setTargetObject(customerService);
        adapter.setTargetMethod("getSampleCustomer");
        return adapter;
    }
}
