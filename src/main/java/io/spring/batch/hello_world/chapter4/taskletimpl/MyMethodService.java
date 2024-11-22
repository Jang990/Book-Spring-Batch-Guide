package io.spring.batch.hello_world.chapter4.taskletimpl;

public class MyMethodService {
    public void printConsole() {
        System.out.println("===> POJO Service Working!");
    }

    public void printConsole(String message) {
        System.out.println("===> POJO Service Working .with={%s}".formatted(message));
    }
}
