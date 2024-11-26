package io.spring.batch.hello_world.chapter7._2;

import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.batch.item.file.transform.LineTokenizer;

import java.util.ArrayList;
import java.util.List;

public class CustomerFileLineTokenizer implements LineTokenizer {
    private final String delimiter = ",";
    private final String[] names = new String[]{
            "firstName",
            "middleInitial",
            "lastName",
            "address",
            "city",
            "state",
            "zipCode"
    };

    private FieldSetFactory fieldSetFactory = new DefaultFieldSetFactory();

    @Override
    public FieldSet tokenize(String line) {
        String[] fields = line.split(delimiter);
        List<String> parsedFields = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            if (i == 4) {
                parsedFields.set(i - 1, parsedFields.get(i - 1) + " " + fields[i]);
                continue;
            }
            parsedFields.add(fields[i]);
        }

        return fieldSetFactory.create(parsedFields.toArray(String[]::new), names);
    }
}
