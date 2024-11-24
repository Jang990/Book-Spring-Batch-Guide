package io.spring.batch.hello_world.chapter6._3_stop.dao;

import io.spring.batch.hello_world.chapter6._3_stop.domain.Transaction;

import java.util.List;

public interface TransactionDao {
    List<Transaction> getTransactionsByAccountNumbers(String accountNumbers);
}
