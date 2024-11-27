package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._10.CustomerService;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.adapter.ItemReaderAdapter;

import static org.junit.jupiter.api.Assertions.*;

class _10_ItemReaderAdapterConfigTest {
    _10_ItemReaderAdapterConfig adapterConfig = new _10_ItemReaderAdapterConfig();

    @Test
    @DisplayName("ItemReader가 기존 서비스 사용하기.")
    void test1() throws Exception {
        ItemReaderAdapter<Chapter7_Customer> adapter =
                adapterConfig.customerItemReader(new CustomerService());

        // customerSerivce에서는 100개의 샘플 데이터를 만들어서 가져올 수 있다.
        for (int i = 0; i < CustomerService.SAMPLE_SIZE; i++) {
            System.out.println(adapter.read());
        }
    }

}