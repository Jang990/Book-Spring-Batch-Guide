package io.spring.batch.hello_world.chapter8._3;

import io.spring.batch.hello_world.chapter8.domain.Chapter8_Customer;

public class UpperCaseNameService {
    public Chapter8_Customer upperCase(Chapter8_Customer customer) {
        Chapter8_Customer newCustomer = new Chapter8_Customer(customer);

        newCustomer.setFirstName(newCustomer.getFirstName().toUpperCase());
        newCustomer.setMiddleInitial(newCustomer.getMiddleInitial().toUpperCase());
        newCustomer.setLastName(newCustomer.getLastName().toUpperCase());

        return newCustomer;
    }
}
