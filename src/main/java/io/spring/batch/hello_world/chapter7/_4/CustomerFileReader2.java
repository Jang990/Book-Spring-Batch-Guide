package io.spring.batch.hello_world.chapter7._4;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_Customer;
import io.spring.batch.hello_world.chapter7.domain.Chapter7_Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import java.util.ArrayList;

@RequiredArgsConstructor
public class CustomerFileReader2 implements ResourceAwareItemReaderItemStream<Chapter7_Customer> {

    private final ResourceAwareItemReaderItemStream<Object> delegate;
    private Object curItem = null;

    /*
    고객A 정보
        고객A의 거래정보 41
        고객A의 거래정보 75
    고객B 정보
        고객B의 거래정보 104

    이런 형식의 파일을 고객정보 단위로 읽어온다.
    read()호출 시 -> 고객A(... 거래[거래 41, 거래 75])
    read()호출 시 -> 고객B(... 거래[거래 104])

    delegate가 고객정보 거래 정보 상관없이 가져오고, 이 클래스는 그 정보를 구분해서 저장한다.
     */
    @Override
    public Chapter7_Customer read() throws Exception {
        if(curItem == null)
            curItem = delegate.read();

        Chapter7_Customer item = (Chapter7_Customer) curItem;
        curItem = null;

        if(item == null)
            return null;

        item.setTransactions(new ArrayList<>());
        while (peek() instanceof Chapter7_Transaction transaction) {
            item.getTransactions().add(transaction);
            curItem = null;
        }
        return item;
    }

    private Object peek() throws Exception {
        if(curItem == null)
            curItem = delegate.read();
        return curItem;
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void setResource(Resource resource) {
        delegate.setResource(resource);
    }
}
