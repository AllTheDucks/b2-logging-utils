package com.alltheducks.logging.logback;

import ch.qos.logback.core.PropertyDefinerBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StaticFactoryPropertyDefiner extends PropertyDefinerBase {

    private String factoryClass;
    private String factoryMethod;
    private String defaultValue;

    @Override
    public String getPropertyValue() {
        final Class<?> clazz;
        try {
            clazz = Class.forName(factoryClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Could not find factory class '%s'", factoryClass));
        }

        final Method method;
        try {
            method = clazz.getMethod(factoryMethod);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Could not find factory method '%s' on factory class '%s'", factoryMethod, factoryClass));
        }

        final Object result;
        try {
            result = method.invoke(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Cannot access factory method '%s' on factory class '%s'", factoryMethod, factoryClass));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(String.format("Factory method '%s' on factory class '%s' threw an exception.", factoryMethod, factoryClass), e.getCause());
        }

        if(result == null) {
            if(defaultValue != null) {
                return defaultValue;
            }
            throw new RuntimeException(String.format("Factory method '%s' on factory class '%s' returned null.", factoryMethod, factoryClass));
        }

        if(!(result instanceof String)) {
            throw new RuntimeException(String.format("Factory method '%s' on factory class '%s' returned '%s'. Expected a String.", factoryMethod, factoryClass, result.getClass().getCanonicalName()));
        }

        return (String)result;
    }

    public String getFactoryClass() {
        return factoryClass;
    }

    public void setFactoryClass(String factoryClass) {
        this.factoryClass = factoryClass;
    }

    public String getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(String factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}