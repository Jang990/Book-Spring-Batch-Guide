package io.spring.batch.hello_world.chapter4.chunk;

import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Random;

public class RandomChunkSizePolicy implements CompletionPolicy {
    private int chunkSize;
    private int totalProcessed;
    private Random random = new Random();

    // 청크의 실패 또는 완료 상태를 기반으로 결정 로직 수행
    @Override
    public boolean isComplete(RepeatContext context, RepeatStatus result) {
        if(RepeatStatus.FINISHED == result)
            return true;
        return isComplete(context);
    }

    // 청크 완료 여부 파악
    @Override
    public boolean isComplete(RepeatContext context) {
        return totalProcessed >= chunkSize;
    }

    // 처음 호출되어, 초기화 역할
    @Override
    public RepeatContext start(RepeatContext parent) {
        this.chunkSize = random.nextInt(20);
        this.totalProcessed = 0;

        System.out.println("===> 랜덤으로 선정된 청크 사이즈 = " + chunkSize);
        return parent;
    }

    // 각 아이템 처리 시 호출되어 내부 상태 갱신
    @Override
    public void update(RepeatContext context) {
        this.totalProcessed++;
    }
}
