package dev.nimbler.ide.component.runner.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jutils.StringUtils;
import org.jutils.process.ProcessRunner;
import org.jutils.threads.ForwardResultToCaller;
import org.mockito.Mockito;

import dev.nimbler.ide.component.common.IDEComponentsFinder;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlTestCase;
import dev.nimbler.ide.component.runner.test.ui.TestRunnerUI;
import dev.nimbler.ide.util.Value;

public class TestRunnerTest {
    
    private static final String EXPECTED_STDOUT =
            "\n"
          + "#TESTLINE START com.neaterbits.ide.component.runner.test.TestRunnerJUnitTest\n"
          + "\n"
          + "#TESTBLOCK MODEL\n"
          +  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
          + "<xmlTestCase>\n"
          + "    <testClass>com.neaterbits.ide.component.runner.test.TestRunnerJUnitTest</testClass>\n"
          + "    <testMethods>\n"
          + "        <testMethod>testMethod1</testMethod>\n"
          + "        <testMethod>testMethod2</testMethod>\n"
          + "        <testMethod>testMethod3</testMethod>\n"
          + "    </testMethods>\n"
          + "</xmlTestCase>\n"
          + "\n"
          + "#TESTBLOCK END MODEL\n"
          + "\n"
          + "#TESTLINE METHOD testMethod1\n"
          + "\n"
          + "#TESTLINE SUCCESS testMethod1\n"
          + "\n"
          + "#TESTLINE METHOD testMethod2\n"
          + "\n"
          + "#TESTLINE SUCCESS testMethod2\n"
          + "\n"
          + "#TESTLINE METHOD testMethod3\n"
          + "\n"
          + "#TESTLINE SUCCESS testMethod3\n"
          + "\n"
          + "#TESTLINE END com.neaterbits.ide.component.runner.test.TestRunnerJUnitTest\n";
    
    private static final XmlTestCase XML_TEST_CASE = new XmlTestCase(
            TestRunnerJUnitTest.class.getName(),
            Arrays.asList("testMethod1", "testMethod2", "testMethod3"));
    
    @Test
    public void testJUnitTestRunner() {

        final StringBuilder appended = new StringBuilder();
        
        final TestRunner testRunner = new TestRunner(appended::append, () -> appended.append('\n'));
        
        testRunner.run(TestRunnerJUnitTest.class.getName(), true);
        
        assertThat(appended.toString()).isEqualTo(EXPECTED_STDOUT);
    }
    
    @Test
    public void testRunnerComponent() {

        final TestRunnerComponent testRunnerComponent = new TestRunnerComponent();

        final IDEComponentsFinder ideComponentsFinder = Mockito.mock(IDEComponentsFinder.class);
        
        final TestRunnerUI testRunnerUI = Mockito.mock(TestRunnerUI.class);
        
        Mockito.when(ideComponentsFinder.findComponentUI(eq(TestRunnerUI.class)))
                .thenReturn(testRunnerUI);

        testRunnerComponent.startOutput();
        
        testRunnerComponent.processStdout(EXPECTED_STDOUT, ideComponentsFinder);

        verifyUIUpdates(ideComponentsFinder, testRunnerUI);
    }

    @Test
    public void testJUnitTestRunnerWithUI() throws IOException {
        
        final List<Runnable> runnables = new ArrayList<>();
        
        final ForwardResultToCaller forwardResultToCaller = runnables::add;
        
        final TestRunnerComponent testRunnerComponent = new TestRunnerComponent();
        
        final Path projectDir = Path.of(System.getProperty("user.dir"));
        
        final Path m2RepositoryDir = Path.of(System.getProperty("user.home"))
                .resolve(".m2").resolve("repository");
        
        final String [] classPath = new String [] {
                projectDir.resolve("target/classes").toString(),
                projectDir.resolve("target/test-classes").toString(),
                
                projectDir.resolve("../component-common/target/classes").toString(),
                projectDir.resolve("../../util/util-base/target/classes").toString(),
                    
                m2RepositoryDir.resolve("junit").resolve("junit").resolve("4.9").resolve("junit-4.9.jar").toString(),

                m2RepositoryDir.resolve("com")
                               .resolve("sun")
                               .resolve("xml")
                               .resolve("bind")
                               .resolve("jaxb-impl")
                               .resolve("2.2.5")
                               .resolve("jaxb-impl-2.2.5.jar").toString(),
                           
               m2RepositoryDir.resolve("javax")
                               .resolve("activation")
                               .resolve("activation")
                               .resolve("1.1")
                               .resolve("activation-1.1.jar").toString(),
                               
               m2RepositoryDir.resolve("javax")
                               .resolve("xml")
                               .resolve("bind")
                               .resolve("jaxb-api")
                               .resolve("2.1")
                               .resolve("jaxb-api-2.1.jar").toString()
                
        }; 
        
        final String [] commandLine = new String [] {
                "java",
//                "-verbose",
                "-cp",
                StringUtils.join(classPath, ';'),
                TestRunner.class.getName(),
                TestRunnerJUnitTest.class.getName(),
                "true"
        };

        // System.out.println("## command line " + Arrays.toString(commandLine));
        
        final IDEComponentsFinder ideComponentsFinder = Mockito.mock(IDEComponentsFinder.class);
        
        final TestRunnerUI testRunnerUI = Mockito.mock(TestRunnerUI.class);
        
        Mockito.when(ideComponentsFinder.findComponentUI(eq(TestRunnerUI.class)))
                .thenReturn(testRunnerUI);

        final Value<Boolean> joined = new Value<>(Boolean.FALSE);
        
        // Run as separate process so can fetch output
        ProcessRunner.run(
                commandLine,
                string -> testRunnerComponent.processStdout(string, ideComponentsFinder),
                string -> {
                    System.err.println("## stderr " + string);
                    testRunnerComponent.processStderr(string, ideComponentsFinder);
                },
                (exitCode, join) -> {

                    assertThat(exitCode).isEqualTo(0);

                    join.run();
                    
                    joined.set(Boolean.TRUE);
                },
                forwardResultToCaller);
        
        
        while (!joined.get()) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }

            while (!runnables.isEmpty()) {
                runnables.remove(0).run();
            }
        }
        
        verifyUIUpdates(ideComponentsFinder, testRunnerUI);
    }
    
    private void verifyUIUpdates(IDEComponentsFinder ideComponentsFinder, TestRunnerUI testRunnerUI) {

        Mockito.verify(ideComponentsFinder, Mockito.atLeastOnce()).findComponentUI(eq(TestRunnerUI.class));
        
        Mockito.verify(testRunnerUI).setModel(eq(XML_TEST_CASE));
        
        Mockito.verify(testRunnerUI).setTestResult("testMethod1", TestStatus.SUCCESS, null, "");
        Mockito.verify(testRunnerUI).setTestResult("testMethod2", TestStatus.SUCCESS, null, "");
        Mockito.verify(testRunnerUI).setTestResult("testMethod3", TestStatus.SUCCESS, null, "");

        Mockito.verifyNoMoreInteractions(ideComponentsFinder, testRunnerUI);
    }
    
}
