package io.spring.batch.hello_world.chapter7._3;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_Transaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TransactionFieldSetMapper implements FieldSetMapper<Chapter7_Transaction> {
    @Override
    public Chapter7_Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
        Chapter7_Transaction transaction = new Chapter7_Transaction();

        transaction.setAccountNumber(fieldSet.readString("accountNumber"));
        transaction.setAmount(fieldSet.readDouble("amount"));
        transaction.setTransactionDate(
                fieldSet.readDate("transactionDate", "yyyy-MM-dd HH:mm:ss")
        );
        return transaction;
    }
}
