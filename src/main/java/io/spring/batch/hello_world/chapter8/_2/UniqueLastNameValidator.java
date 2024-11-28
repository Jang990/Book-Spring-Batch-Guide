package io.spring.batch.hello_world.chapter8._2;

import io.spring.batch.hello_world.chapter8.domain.Chapter8_Customer;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.util.HashSet;
import java.util.Set;

// ItemStreamSupport의 open, update는 이제 사용되지 않는다. -> 나중에 필요할 떄 알아보자.
public class UniqueLastNameValidator implements Validator<Chapter8_Customer>, ItemStream {
    private Set<String> lastNames = new HashSet<>();

    @Override
    public void validate(Chapter8_Customer value) throws ValidationException {
        // 책 확인.
    }

}
