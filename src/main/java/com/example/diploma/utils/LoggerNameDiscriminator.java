package com.example.diploma.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;

public class LoggerNameDiscriminator implements Discriminator<ILoggingEvent> {

    private static final String KEY = "classLog";
    private boolean started;

    @Override
    public String getDiscriminatingValue(ILoggingEvent event) {
        String loggerName = event.getLoggerName();
        if (loggerName == null || loggerName.isBlank()) {
            return "application";
        }
        return loggerName;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}

