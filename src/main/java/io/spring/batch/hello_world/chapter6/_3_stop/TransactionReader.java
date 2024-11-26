package io.spring.batch.hello_world.chapter6._3_stop;

import io.spring.batch.hello_world.chapter6._3_stop.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.file.transform.FieldSet;

@RequiredArgsConstructor
public class TransactionReader implements ItemStreamReader<Transaction> {
    private final ItemStreamReader<FieldSet> fieldSetReader;
    private StepExecution stepExecution;
    private int recordCount = 0;
    private int expectedRecordCount = 0;

    // 파일 정보 읽어오기.
    @Override
    public Transaction read() throws Exception {
        return process(fieldSetReader.read());
    }

    private Transaction process(FieldSet fieldSet) {
        if(fieldSet == null)
            return null;
        /*
        if(recordCount == 25)
            throw new ParseException("의도하지 않은 상황"); // SpringBatch는 이 예외를 실패로 간주.
        // 중지와는 엄연히 다르다. STOPPED가 아닌 FAILED가 저장된다.
        */
        if (fieldSet.getFieldCount() <= 1) { // 마지막 줄일 때.
            expectedRecordCount = fieldSet.readInt(0);
            if(recordCount != expectedRecordCount)
                stepExecution.setTerminateOnly();
            return null;
        }

        Transaction result = new Transaction();
        result.setAccountNumber(fieldSet.readString(0));
        result.setTimestamp(fieldSet.readDate(1, "yyyy-MM-DD HH:mm:ss"));
        result.setAmount(fieldSet.readDouble(2));
        recordCount++;
        return result;
    }

    /*
    // 방법 1
    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        // 유효성 검사
        if(recordCount != expectedRecordCount)
            return ExitStatus.STOPPED;
        return stepExecution.getExitStatus();
    }
    */

    // 방법 2
    @BeforeStep
    public void  beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        fieldSetReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        fieldSetReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        fieldSetReader.close();
    }
}
