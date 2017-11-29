package com.alltheducks.logging.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class LogbackUtil {

    public static void reloadLogbackConfiguration() {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        ContextInitializer ci = new ContextInitializer(loggerContext);
        URL url = ci.findURLOfDefaultConfigurationFile(true);
        loggerContext.reset();
        try {
            ci.configureByResource(url);
        } catch (JoranException e) {
            throw new RuntimeException(e);
        }
    }

}
