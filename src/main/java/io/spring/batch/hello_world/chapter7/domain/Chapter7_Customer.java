package io.spring.batch.hello_world.chapter7.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter7_Customer {
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    /*private String street; // addressNumber와 통합됨 */
    private String city;
    private String state;
    private String zipCode;
}
