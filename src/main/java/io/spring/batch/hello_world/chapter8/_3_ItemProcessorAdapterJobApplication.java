package io.spring.batch.hello_world.chapter8;

import io.spring.batch.hello_world.chapter8._3.UpperCaseNameService;
import io.spring.batch.hello_world.chapter8.domain.Chapter8_Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

//@SpringBootApplication
@RequiredArgsConstructor
public class _3_ItemProcessorAdapterJobApplication {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
	@StepScope
	public FlatFileItemReader<Chapter8_Customer> customerItemReader(
			@Value("#{jobParameters['customerFile']}") Resource inputFile) {

		return new FlatFileItemReaderBuilder<Chapter8_Customer>()
				.name("customerItemReader")
				.delimited()
				.names(
                        "firstName", "middleInitial", "lastName",
						"address", "city", "state", "zip")
				.targetType(Chapter8_Customer.class)
				.resource(inputFile)
				.build();
	}

	@Bean
	public ItemWriter<Chapter8_Customer> itemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

	@Bean
	public ItemProcessorAdapter<Chapter8_Customer, Chapter8_Customer> itemProcessorAdapter() {
		ItemProcessorAdapter<Chapter8_Customer, Chapter8_Customer> adapter =
				new ItemProcessorAdapter<>();
		adapter.setTargetObject(new UpperCaseNameService()); // 샘플 코드기 떄문에 쉽게...
		adapter.setTargetMethod("upperCase");

		return adapter;
	}

	@Bean
	public Step copyFileStep() {
		return new StepBuilder("copyFileStep", jobRepository)
				.<Chapter8_Customer, Chapter8_Customer>chunk(5, transactionManager)
				.reader(customerItemReader(null))
				.processor(itemProcessorAdapter())
				.writer(itemWriter())
				.build();
	}

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
                .start(copyFileStep())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(_3_ItemProcessorAdapterJobApplication.class, "customerFile=chapter8/input/customer-valid.csv");
    }
}
