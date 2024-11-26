package io.spring.batch.hello_world.chapter7.domain;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Chapter7_Transaction {
    private static final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    private String accountNumber;
    private Date transactionDate;
    private Double amount;
}
