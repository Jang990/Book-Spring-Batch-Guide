package io.spring.batch.hello_world.chapter7;

import io.spring.batch.hello_world.chapter7._7.CustomerRowMapper;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_JDBC_Customer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class _7_JdbcPagingConfig {
    // 페이징한 레코드 처리
    @Bean
    public JdbcPagingItemReader<Chapter7_JDBC_Customer> customerJdbcItemReader(
            DataSource dataSource,
            PagingQueryProvider pagingQueryProvider,
            @Value("#{jobParameter['city']}") String city) {
        Map<String, Object> parameterValues = new HashMap<>(1);
        parameterValues.put("city", city);

        return new JdbcPagingItemReaderBuilder<Chapter7_JDBC_Customer>()
                .name("customerJdbcItemReader")
                .dataSource(dataSource)
                .queryProvider(pagingQueryProvider)
                .parameterValues(parameterValues)
                .pageSize(10)
                .rowMapper(new CustomerRowMapper())
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean pagingQueryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setSelectClause("select *");
        factoryBean.setFromClause("from ch7_customer");
        factoryBean.setWhereClause("where city = :city");
        factoryBean.setSortKey("lastName");
        factoryBean.setDataSource(dataSource);

        return factoryBean;
    }
}
