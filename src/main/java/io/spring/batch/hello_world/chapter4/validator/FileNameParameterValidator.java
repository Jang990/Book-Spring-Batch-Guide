package io.spring.batch.hello_world.chapter4.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class FileNameParameterValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = parameters.getString("fileName");

        if(!StringUtils.hasText(fileName))
            throw new JobParametersInvalidException("filename parameter is missing");
        if (!StringUtils.endsWithIgnoreCase(fileName, "csv"))
            throw new JobParametersInvalidException("fileName parameter does not use the csv file extension");
    }
}
