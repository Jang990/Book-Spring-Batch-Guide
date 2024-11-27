package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_JPA_Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.util.Collections;

public class _8_JpaConfig {
    @Bean
    @StepScope
    public JpaPagingItemReader<Chapter7_JPA_Customer> customerItemReader(
            EntityManagerFactory emf,
            @Value("#{jobParameter['city']}") String city) {
        return new JpaPagingItemReaderBuilder<Chapter7_JPA_Customer>()
                .name("customerItemReader")
                .entityManagerFactory(emf)

                // 쿼리를 만드는 방식은 둘 중 하나를 선택하면 된다.
                .queryString("select c from Chapter7_JPA_Customer c where c.city = :city")
//                .queryProvider(new CustomerByCityQueryProvider(city))

                .parameterValues(Collections.singletonMap("city", city))
                .build();
    }

    @RequiredArgsConstructor
    static class CustomerByCityQueryProvider extends AbstractJpaQueryProvider {
        private final String cityName;

        @Override
        public Query createQuery() {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("""
                    select c from Chapter7_JPA_Customer c
                    where c.city = :city
                    """);
            query.setParameter("city", cityName);
            return query;
        }

        @Override
        public void afterPropertiesSet() throws Exception {}
    }
}
