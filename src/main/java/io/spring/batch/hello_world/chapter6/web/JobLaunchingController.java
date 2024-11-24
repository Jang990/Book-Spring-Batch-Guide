package io.spring.batch.hello_world.chapter6.web;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
/*import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;*/

import java.util.Map;

/*@RestController*/
@RequiredArgsConstructor
public class JobLaunchingController {
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final ApplicationContext ac;

    /*{"name": "job", "jobParameters" : {"foo": "bar","baz": "quix"}}*/

    /*@PostMapping("/run")*/ // 동일 파라미터 재시도 불가능
    public ExitStatus runJob(/*@RequestBody*/ JobLaunchRequest request) throws Exception {
        Job job = ac.getBean(request.getName(), Job.class);
        return jobLauncher.run(job, toJobParameter(request.getJobParameters()))
                .getExitStatus();
    }

    /*@PostMapping("/run2")*/ // RunIdIncrementer를 적용하여 동일 파라미터 재시도 가능
    public ExitStatus runJob2(/*@RequestBody*/ JobLaunchRequest request) throws Exception {
        Job job = ac.getBean(request.getName(), Job.class);

        JobParameters requestedParameters = toJobParameter(request.getJobParameters());
        JobParameters jobParameters = new JobParametersBuilder(requestedParameters, jobExplorer)
                .getNextJobParameters(job) // run.id가 있는 파라미터 생성
                .toJobParameters();

        return jobLauncher.run(job, jobParameters)
                .getExitStatus();
    }

    private JobParameters toJobParameter(Map<String, String> jobParameters) {
        JobParametersBuilder builder = new JobParametersBuilder();
        jobParameters.forEach(builder::addString);
        return builder.toJobParameters();
    }

}
