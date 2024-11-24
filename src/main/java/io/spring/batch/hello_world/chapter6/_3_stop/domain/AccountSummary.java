package io.spring.batch.hello_world.chapter6._3_stop.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 계좌 정보
 */
@Getter
@Setter
public class AccountSummary {
    private int id;
    private String accountNumber;
    private Double currentBalance;
}
