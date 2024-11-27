package io.spring.batch.hello_world.chapter7._10;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_JPA_Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerService {
    public static final int SAMPLE_SIZE = 100;
    private List<Chapter7_JPA_Customer> customers;
    private int curIndex;

    private String [] firstNames = {"Michael", "Warren", "Ann", "Terrence",
            "Erica", "Laura", "Steve", "Larry"};
    private String middleInitial = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String [] lastNames = {"Gates", "Darrow", "Donnelly", "Jobs",
            "Buffett", "Ellison", "Obama"};
    private String [] streets = {"4th Street", "Wall Street", "Fifth Avenue",
            "Mt. Lee Drive", "Jeopardy Lane",
            "Infinite Loop Drive", "Farnam Street",
            "Isabella Ave", "S. Greenwood Ave"};
    private String [] cities = {"Chicago", "New York", "Hollywood", "Aurora",
            "Omaha", "Atherton"};
    private String [] states = {"IL", "NY", "CA", "NE"};

    private Random generator = new Random();

    public CustomerService() {
        curIndex = 0;
        customers = new ArrayList<>();
        for(int i = 0; i < SAMPLE_SIZE; i++) {customers.add(buildCustomer());}
    }

    private Chapter7_JPA_Customer buildCustomer() {
        Chapter7_JPA_Customer customer = new Chapter7_JPA_Customer();

        customer.setId((long) generator.nextInt(Integer.MAX_VALUE));
        customer.setFirstName(
                firstNames[generator.nextInt(firstNames.length - 1)]);
        customer.setMiddleInitial(
                String.valueOf(middleInitial.charAt(
                        generator.nextInt(middleInitial.length() - 1))));
        customer.setLastName(
                lastNames[generator.nextInt(lastNames.length - 1)]);
        customer.setAddress(generator.nextInt(9999) + " " +
                streets[generator.nextInt(streets.length - 1)]);
        customer.setCity(cities[generator.nextInt(cities.length - 1)]);
        customer.setState(states[generator.nextInt(states.length - 1)]);
        customer.setZipCode(String.valueOf(generator.nextInt(99999)));

        return customer;
    }

    public Chapter7_JPA_Customer getSampleCustomer() {
        if(curIndex >= SAMPLE_SIZE)
            // 입력 데이터를 모두 처리하면 무조건 null을 반환해줘야 함.
            // = SpringBatch에게 해당 스텝의 입력을 모두 소비했음을 나타내는 표현
            return null;
        return customers.get(curIndex++);
    }
}
