package dev.nimbler.ide.component.common.runner.model.test;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;

import org.jutils.coll.Coll;

public class TestCase {
    
    private final Class<?> testClass;
    private final Constructor<?> constructor;

    private final List<TestMethod> testMethods;
    private final List<TestBefore> beforeMethods;
    private final List<TestAfter> afterMethods;

    public TestCase(
            Class<?> testClass,
            Constructor<?> constructor,
            List<TestMethod> testMethods,
            List<TestBefore> beforeMethods,
            List<TestAfter> afterMethods) {

        Objects.requireNonNull(testClass);
        Objects.requireNonNull(constructor);
        Objects.requireNonNull(testMethods);
        
        this.testClass = testClass;
        this.constructor = constructor;
        
        this.testMethods = Coll.immutable(testMethods);
        this.beforeMethods = Coll.immutable(beforeMethods);
        this.afterMethods = Coll.immutable(afterMethods);
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public List<TestMethod> getTestMethods() {
        return testMethods;
    }

    public List<TestBefore> getBeforeMethods() {
        return beforeMethods;
    }

    public List<TestAfter> getAfterMethods() {
        return afterMethods;
    }
}
