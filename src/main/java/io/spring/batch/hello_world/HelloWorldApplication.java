package io.spring.batch.hello_world;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableBatchProcessing <- DefaultBatchConfiguration가 생겨서 이젠 필수 아님.
@SpringBootApplication(scanBasePackages = "io.spring.batch.hello_world.chapter7")
public class HelloWorldApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApplication.class, args);
	}
}
