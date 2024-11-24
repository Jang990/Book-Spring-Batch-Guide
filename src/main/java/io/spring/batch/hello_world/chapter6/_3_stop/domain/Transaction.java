package io.spring.batch.hello_world.chapter6._3_stop.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 거래정보
 */
@Getter
@Setter
@ToString
public class Transaction {
    private String accountNumber;
    private Date timestamp;
    private double amount;
    // ACCOUNT_SUMMARY_ID (FK)는 생략. DB상에 존재
}
