package io.spring.batch.hello_world;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//@EnableBatchProcessing <- DefaultBatchConfiguration가 생겨서 이젠 필수 아님.
@SpringBootApplication
public class HelloWorldApplication /*implements CommandLineRunner*/ {
	@Autowired
	private Job job;

	@Autowired
	private JobLauncher jobLauncher;

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApplication.class, args);
	}

	/*@Override
	public void run(String... args) throws Exception {
		JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
		System.out.println("Job Execution Status: " + jobExecution.getStatus());
	}*/
}
