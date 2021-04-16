package dev.nimbler.ide.component.runner.test.junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dev.nimbler.ide.component.common.runner.model.test.TestAfter;
import dev.nimbler.ide.component.common.runner.model.test.TestBefore;
import dev.nimbler.ide.component.common.runner.model.test.TestCase;
import dev.nimbler.ide.component.common.runner.model.test.TestMethod;

public class JUnitTestScanner {

    public static TestCase scan(Class<?> unitTestClass, boolean sortMethods) throws NoSuchMethodException, SecurityException {
        
        final List<TestMethod> testMethods = new ArrayList<>();
        final List<TestBefore> beforeMethods = new ArrayList<>();
        final List<TestAfter> afterMethods = new ArrayList<>();
        
        for (Class<?> cl = unitTestClass; !cl.equals(Object.class); cl = cl.getSuperclass()) {
            
            for (Method method : unitTestClass.getDeclaredMethods()) {
                
                if ((method.getModifiers() & Modifier.PRIVATE) != 0) {
                    continue;
                }
                
                if (method.isAnnotationPresent(Test.class)) {
                    testMethods.add(new TestMethod(method));
                }
                else if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.add(new TestBefore(method));
                }
                else if (method.isAnnotationPresent(After.class)) {
                    afterMethods.add(new TestAfter(method));
                }
            }
        }
        
        final Constructor<?> constructor = unitTestClass.getConstructor();
        
        if (sortMethods) {
            testMethods.sort((m1, m2) -> m1.getMethod().getName().compareTo(m2.getMethod().getName()));
        }

        return new TestCase(
                unitTestClass,
                constructor,
                testMethods,
                beforeMethods,
                afterMethods);
    }
}