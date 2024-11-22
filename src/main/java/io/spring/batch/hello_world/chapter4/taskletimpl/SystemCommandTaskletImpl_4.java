package io.spring.batch.hello_world.chapter4.taskletimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.SimpleSystemProcessExitCodeMapper;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class SystemCommandTaskletImpl_4 {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job systemCommandJob() {
        return new JobBuilder("SystemCommand_Job", jobRepository)
                .start(lambdaStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step lambdaStep() {
        return new StepBuilder("SystemCommand_step1", jobRepository)
                .tasklet(systemCommandTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public SystemCommandTasklet systemCommandTasklet() {
        SystemCommandTasklet tasklet = new SystemCommandTasklet();
        tasklet.setCommand("rm-rf /tmp.txt"); // 이 명령어 실행
        tasklet.setTimeout(5000); // 5초내로 완료되지 않으면 실패
        tasklet.setInterruptOnCancel(true); // 작업이 취소 됐을 때 명령어 강제 중단 여부 설정

        // 종료 코드 매핑 방식 설정. 현재 Mapper는 0이면 FINISH 다른 종료 값은 FAILED
        tasklet.setSystemProcessExitCodeMapper(touchCodeMapper());

        // 시스템 명령어는 비동기 처리기 떄문에 완료 여부를 파악해야함.
        tasklet.setTerminationCheckInterval(5000); // 완료 여부를 몇 초마다 확인할 것인지

        // 시스템 명령을 실행하는 실행 방법
        // 시스템 명령 시 문제가 발생한다면 락이 발생할 수 있으니 동기 방식은 피하기
        tasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());

        tasklet.setEnvironmentParams(new String[]{ // 환경 변수 목록
                "JAVA_HOME=/java",
                "BATCH_HOME=/Users/batch"
        });

        return tasklet;
    }

    @Bean
    public SimpleSystemProcessExitCodeMapper touchCodeMapper() {
        return new SimpleSystemProcessExitCodeMapper();
    }
}
