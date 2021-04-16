package dev.nimbler.ide.component.common.runner.model.test;

import java.lang.reflect.Method;
import java.util.Objects;

public abstract class BaseMethod {

    private final Method method;

    public BaseMethod(Method method) {

        Objects.requireNonNull(method);
        
        if (method.getParameterCount() > 0) {
            throw new IllegalArgumentException();
        }

        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
