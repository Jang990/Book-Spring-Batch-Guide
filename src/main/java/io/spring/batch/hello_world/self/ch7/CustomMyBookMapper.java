package io.spring.batch.hello_world.self.ch7;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomMyBookMapper implements FieldSetMapper<MyBook> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public MyBook mapFieldSet(FieldSet fieldSet) throws BindException {
        MyBook result = new MyBook();

        result.setId(toLong(fieldSet.readString("id")));
        result.setTitle(fieldSet.readString("title"));
        result.setAuthor(fieldSet.readString("author"));
        result.setPublisher(fieldSet.readString("publisher"));
        result.setPublicationYear(toInt(fieldSet.readString("publicationYear")));
        result.setIsbn(fieldSet.readString("isbn"));
        result.setSetIsbn(fieldSet.readString("setIsbn"));
        result.setAdditionalCode(fieldSet.readString("additionalCode"));
        result.setVolume(toInt(fieldSet.readString("volume")));
        result.setSubjectCode(fieldSet.readString("subjectCode"));
        result.setNumberOfBooks(fieldSet.readInt("numberOfBooks"));
        result.setLoanCount(toInt(fieldSet.readString("loanCount")));
        result.setRegistrationDate(LocalDate.parse(fieldSet.readString("registrationDate"), formatter));

        return result;
    }

    private long toLong(String value) {
        return value.isBlank() ? 0 : Long.parseLong(value);
    }

    private int toInt(String value) {
        return value.isBlank() ? 0 : Integer.parseInt(value);
    }
}
