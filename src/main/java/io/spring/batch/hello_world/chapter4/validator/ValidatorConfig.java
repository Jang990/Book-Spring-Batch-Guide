package io.spring.batch.hello_world.chapter4.validator;

import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ValidatorConfig {

    // 디폴트 구성.
    @Bean
    public JobParametersValidator defaultValidator() {
        DefaultJobParametersValidator validator = new DefaultJobParametersValidator();

        validator.setRequiredKeys(new String[]{"fileName"});
        validator.setOptionalKeys(new String[]{"name", "currentDate"});

        return validator;
    }


    // 디폴트 + 커스텀 파일명 validator
    @Bean
    public CompositeJobParametersValidator compositeValidator(
            JobParametersValidator defaultValidator,
            JobParametersValidator customFileNameValidator) {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(
                Arrays.asList(new FileNameParameterValidator(), defaultValidator)
        );
        return validator;
    }
}
