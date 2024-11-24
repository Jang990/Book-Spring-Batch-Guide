package io.spring.batch.hello_world.chapter6._3_stop;

import io.spring.batch.hello_world.chapter6._3_stop.dao.TransactionDao;
import io.spring.batch.hello_world.chapter6._3_stop.domain.AccountSummary;
import io.spring.batch.hello_world.chapter6._3_stop.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

@RequiredArgsConstructor
public class TransactionApplierProcessor implements ItemProcessor<AccountSummary, AccountSummary> {
    private final TransactionDao transactionDao;

    // 잔액 최신화
    @Override
    public AccountSummary process(AccountSummary summary) {
        List<Transaction> transactions = transactionDao
                .getTransactionsByAccountNumbers(summary.getAccountNumber());

        for (Transaction transaction : transactions) {
            summary.setCurrentBalance(
                    summary.getCurrentBalance() + transaction.getAmount()
            );
        }
        return summary;
    }
}
