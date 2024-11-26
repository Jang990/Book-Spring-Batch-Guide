package io.spring.batch.hello_world.chapter7._2;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFiledSetMapper implements FieldSetMapper<Chapter7_Customer> {
    @Override
    public Chapter7_Customer mapFieldSet(FieldSet fieldSet) throws BindException {
        Chapter7_Customer customer = new Chapter7_Customer();

        customer.setAddress(
                fieldSet.readString("addressNumber")  + " " +
                        fieldSet.readString("street")
        );

        customer.setCity(fieldSet.readString("city"));
        customer.setFirstName(fieldSet.readString("firstName"));
        customer.setLastName(fieldSet.readString("lastName"));
        customer.setMiddleInitial(fieldSet.readString("middleInitial"));
        customer.setState(fieldSet.readString("state"));
        customer.setZipCode(fieldSet.readString("zipCode"));
        return customer;
    }
}
