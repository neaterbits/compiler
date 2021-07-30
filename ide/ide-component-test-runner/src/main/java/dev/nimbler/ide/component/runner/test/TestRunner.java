package dev.nimbler.ide.component.runner.test;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;

import dev.nimbler.ide.component.common.runner.model.test.BaseMethod;
import dev.nimbler.ide.component.common.runner.model.test.TestAfter;
import dev.nimbler.ide.component.common.runner.model.test.TestBefore;
import dev.nimbler.ide.component.common.runner.model.test.TestCase;
import dev.nimbler.ide.component.common.runner.model.test.TestMethod;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlStackTrace;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlStackTraceEntry;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlTestCase;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlThrowable;
import dev.nimbler.ide.component.runner.test.junit.JUnitTestScanner;

public class TestRunner {
    
    private final Consumer<String> append;
    private final Runnable println;

    private static class TestException extends Exception {

        private static final long serialVersionUID = 1L;

        TestException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static void main(String [] args) {
        
        final String testClass = args[0];
        
        final boolean sort = args.length > 1 && "true".equals(args[1]);
        
        final TestRunner testRunner = new TestRunner(System.out::append, System.out::println);
        
        testRunner.run(testClass, sort);
    }
    
    TestRunner(Consumer<String> append, Runnable println) {
        
        Objects.requireNonNull(append);
        Objects.requireNonNull(println);

        this.append = append;
        this.println = println;
    }

    void run(String testClass, boolean sortMethods) {

        emitTestCaseLine(TestLineType.START, testClass);
        
        try {
            final Class<?> cl = Class.forName(testClass);
            
            final TestCase testCase = JUnitTestScanner.scan(cl, sortMethods);
            
            emitModel(testCase);
            
            runTests(testCase);
            
        } catch (Throwable t) {
            
            emitThrowable(TestLineType.EXCEPTION, t);
        }
        finally {
            emitTestCaseLine(TestLineType.END, testClass);
        }
    }
    
    private static Set<Long> getThreads() {
        final Thread [] threads = new Thread[1000];

        final int numThreads = Thread.enumerate(threads);
        if (numThreads >= threads.length) {
            throw new IllegalStateException();
        }

        return Arrays.stream(threads, 0, numThreads)
                .map(Thread::getId)
                .collect(Collectors.toSet());
    }
    
    private void runTests(TestCase testCase)
            throws InstantiationException,
                   IllegalAccessException,
                   InvocationTargetException {
        for (TestMethod testMethod : testCase.getTestMethods()) {
            
            emitTestCaseLine(TestLineType.METHOD, testMethod.getMethod().getName());
            
            final Object instance = testCase.getConstructor().newInstance();
        
            final Set<Long> beforeThreads = getThreads(); 

            try {
                for (TestBefore beforeMethod : testCase.getBeforeMethods()) {
                    execute(instance, beforeMethod);
                }
    
                execute(instance, testMethod);
    
                for (TestAfter afterMethod : testCase.getAfterMethods()) {
                    execute(instance, afterMethod);
                }

                final Set<Long> afterThreads = getThreads();
                
                if (!beforeThreads.equals(afterThreads)) {
                    throw new IllegalStateException();
                }

                emitTestCaseLine(TestLineType.SUCCESS, testMethod.getMethod().getName());
            }
            catch (TestException ex) {
                // Already emmited exception
            }
        }
    }
    
    private void emitModel(TestCase testCase) {

        final XmlTestCase xmlTestCase = new XmlTestCase();
        
        xmlTestCase.setTestClass(testCase.getTestClass().getName());
        
        xmlTestCase.setTestMehods(testCase.getTestMethods().stream()
                                            .map(m -> m.getMethod().getName())
                                            .collect(Collectors.toList()));
        
        emitTestBlock(TestLineType.MODEL);
        
        emitXmlBlock(xmlTestCase);
        
        emitTestBlockEnd(TestLineType.MODEL);
    }
    
    private void emitXmlBlock(Object obj) {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        JAXB.marshal(obj, baos);
        
        emitBlock(new String(baos.toByteArray()));
    }
    
    private void emitThrowable(TestLineType type, Throwable t) {

        emitTestBlock(type);

        emitXmlBlock(makeXmlThrowable(t));
        
        emitTestBlockEnd(type);
    }

    private static XmlThrowable makeXmlThrowable(Throwable t) {

        final XmlThrowable xmlThrowable
            = new XmlThrowable(
                    t.getClass(),
                    t.getMessage(),
                    makeXmlStackTrace(t.getStackTrace()),
                    t.getCause() != null ? makeXmlThrowable(t.getCause()) : null);
        
        return xmlThrowable;
    }
    
    
    private static XmlStackTrace makeXmlStackTrace(StackTraceElement [] stackTrace) {
        
        return new XmlStackTrace(Arrays.stream(stackTrace)
                .map(e -> new XmlStackTraceEntry(e.getClassName(), e.getMethodName(), e.getLineNumber()))
                .collect(Collectors.toList()));
    }
    
    private void execute(Object instance, BaseMethod baseMethod) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, TestException {
        
        try {
            baseMethod.getMethod().invoke(instance);
        }
        catch (AssertionError e) {
            emitThrowable(TestLineType.FAILURE, e);

            throw new TestException("Caught exception while invoking", e);
        }
        catch (Throwable t) {
            emitThrowable(TestLineType.ERROR, t);
            
            throw new TestException("Caught exception while invoking", t);
        }
    }
    
    private void println() {
        println.run();
    }
    
    private void append(String string) {
        
        Objects.requireNonNull(string);
        
        append.accept(string);
    }
    
    private void emitTestCaseLine(TestLineType type, String text) {
        
        println();
        append("#TESTLINE ");
        append(type.name());
        append(" ");
        append(text);
        println();
    }
    
    private void emitTestBlock(TestLineType type) {
        
        println();
        append("#TESTBLOCK ");
        append(type.name());
        println();
    }

    private void emitBlock(String text) {
        append(text);
    }
    
    private void emitTestBlockEnd(TestLineType type) {
        
        println();
        append("#TESTBLOCK END ");
        append(type.name());
        println();
    }
}
