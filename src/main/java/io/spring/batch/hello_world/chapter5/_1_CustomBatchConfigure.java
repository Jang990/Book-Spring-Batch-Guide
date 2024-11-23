package io.spring.batch.hello_world.chapter5;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

//@Configuration
@RequiredArgsConstructor
public class _1_CustomBatchConfigure extends DefaultBatchConfiguration {
    /*
    만약 애플리케이션과 다른 @BatchDataSource를 붙인 DataSource를 빈으로 재정의한 경우

    @BatchDataSource @Bean
    public DataSource dataSourceForBatch() { ... }
     */
    @BatchDataSource
    private final DataSource dataSource;

    // 스프링 부트 3.x용 설정
    @SneakyThrows
    @Bean
    public JobRepository jobRepository() {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setDatabaseType(DatabaseType.MYSQL.getProductName());
        factoryBean.setTablePrefix("FOO_");
        factoryBean.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @SneakyThrows
    @Bean
    public JobExplorer jobExplorer() {
        JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTablePrefix("FOO_");
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
