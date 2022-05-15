package ru.test.annotation.battle.infrastructure;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.utils.Logger;

@Bean(isInfrastructure = true)
public class SimpleLogger implements Logger {
    @Override
    public void log(String prefix, String message) {
        log(prefix, message, null);
    }

    @Override
    public void debug(String prefix, String message) {
        log(prefix, message, null);
    }

    @Override
    public void error(String prefix, String message) {
        log(prefix, message, null);
    }

    @Override
    public void error(String prefix, String message, Throwable throwable) {
        log(prefix, message, throwable);
    }

    @Override
    public void error(String prefix, Throwable throwable) {

    }

    private void log(String prefix, String message, @Nullable Throwable throwable) {
        System.out.println("[%s] %s".formatted(prefix, message));

        if (throwable != null) {
            throwable.printStackTrace();
        }
    }
}
