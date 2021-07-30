package dev.nimbler.ide.component.runner.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import javax.xml.bind.JAXB;

import org.jutils.StringUtils;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledFileResourcePath;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.IDEComponentsFinder;
import dev.nimbler.ide.component.common.runner.RunnableLanguage;
import dev.nimbler.ide.component.common.runner.RunnerComponent;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlTestCase;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlThrowable;
import dev.nimbler.ide.component.runner.test.ui.TestRunnerUI;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.common.types.TypeName;

public class TestRunnerComponent implements RunnerComponent {
    
    private static final Boolean DEBUG = false; 
    
    private final OutputBuffer stdout;
    private final StringBuilder block;
    
    private boolean stdoutWithinBlock;
    private String curMethod;

    public TestRunnerComponent() {
        this.stdout = new OutputBuffer();
        this.block = new StringBuilder();
    }
    
    @Override
    public boolean isRunnable(SourceFileResourcePath sourceFile, SourceFileModel sourceFileModel,
            RunnableLanguage sourceFileLanguage) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRunnable(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public TypeName getRunnableType(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage) {

        return new TypeName(
                StringUtils.split(TestRunner.class.getPackageName(), '.'),
                null,
                TestRunner.class.getName());
    }

    @Override
    public String[] getCommandLineForRunning(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel,
            TypeName entryPointType,
            String[] programArguments,
            String[] vmArguments,
            CodeAccess codeAccess) {
        
        final String [] namespace
            = sourceFile.getNamespacePath().getNamespaceResource().getNamespace();
        
        final String [] all = Arrays.copyOf(namespace, namespace.length + 1);
        
        final String sourceFileName = sourceFile.getName();
        
        all[all.length - 1] = sourceFileName.substring(0, sourceFile.length() - ".java".length());
                
        return RunnerComponent.getCommandLineFromArguments(
                sourceFile,
                sourceFileModel,
                entryPointType,
                new String [] { StringUtils.join(all, '.') },
                vmArguments,
                codeAccess);
    }

    
    @Override
    public void startOutput() {
        
        if (DEBUG) {
            System.out.println("## startOutput");
        }

        stdout.reset();
        block.setLength(0);
    }

    private static final String TEST_LINE_MARKER = "#TESTLINE ";
    private static final String TEST_BLOCK_END_MARKER = "TESTBLOCK END ";
    

    @Override
    public String processStdout(String string, IDEComponentsFinder componentsFinder) {
        
        if (DEBUG) {
            System.out.println("## processStdout '" + string + "'");
        }

        final TestRunnerUI ui = componentsFinder.findComponentUI(TestRunnerUI.class);
        
        final StringBuilder sb = new StringBuilder();
        
        stdout.add(string);
        
        final String lines = stdout.retrieveLines();

        if (DEBUG) {
            System.out.println("## got lines '" + lines + "'");
        }
        
        if (lines != null) {
            final BufferedReader reader = new BufferedReader(new StringReader(lines));

            if (stdoutWithinBlock) {
                readBlockContent(curMethod, reader, sb, ui);
            }
            else {
                processLines(lines, sb, ui);
            }
        }

        return sb.toString();
    }
    
    private void processLines(String lines, StringBuilder sb, TestRunnerUI ui) {
        
        final BufferedReader reader = new BufferedReader(new StringReader(lines));

        try {
            String line;
            
            while (null != (line = (reader.readLine()))) {
         
                if (DEBUG) {
                    System.out.println("## start line " + line);
                }
            
                if (line.startsWith(TEST_LINE_MARKER)) {

                    removePriorNewLine(sb);
                    
                    final TestLineType type = getTestLineTypeUntilSpace(line, TEST_LINE_MARKER.length());
                    
                    switch (type) {
                    
                    case START:
                        break;
                        
                    case METHOD:
                        curMethod = line.substring(TEST_LINE_MARKER.length() + TestLineType.METHOD.name().length() + 1);
                        break;
                        
                    case SUCCESS:
                        ui.setTestResult(curMethod, TestStatus.SUCCESS, null, sb.toString());
                        break;
                        
                    case END:
                        break;
                        
                    default:
                        throw new IllegalStateException("");
                    }
                }
                else if (line.startsWith("#TESTBLOCK ")) {
                    removePriorNewLine(sb);

                    this.stdoutWithinBlock = true;
                    
                    readBlockContent(curMethod, reader, sb, ui);
                }
                else {
                    sb.append(line);

                    if (DEBUG) {
                        System.out.println("## append line '" + sb.toString() + "'");
                    }

                    sb.append(System.lineSeparator());
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    private static void removePriorNewLine(StringBuilder sb) {

        if (sb.charAt(sb.length() - 1) == '\n') {
            
            final int newLength;
            
            if (sb.charAt(sb.length() - 2) == '\r') {
                newLength = sb.length() - 2;
            }
            else {
                newLength = sb.length() - 1;
            }
            
            sb.setLength(newLength);
        }
        else {
            throw new IllegalStateException();
        }
    }
    
    private void readBlockContent(
            String method,
            BufferedReader reader,
            StringBuilder sb,
            TestRunnerUI ui) {

        char lastChar = 0;
        
        try {
            for (;;) {
             
                final int intC = reader.read();
                
                final char c = (char)intC;
    
                if (intC < 0) {
                    break;
                }
                else if (c == '#' && lastChar == '\n') {
                    
                    reader.mark(1000);
                    
                    final String testLine = reader.readLine();
                    
                    if (testLine.startsWith(TEST_BLOCK_END_MARKER)) {
                        // End of block
                        removePriorNewLine(block);
    
                        processBlock(method, reader, testLine, block, sb, ui);
    
                        block.setLength(0);
                        
                        this.stdoutWithinBlock = false;
                        break;
                    }
                    else {
                        reader.reset();
                     
                        // Within block
                        block.append(c);
                    }
                }
                else {
                    // Within block
                    block.append(c);
                }
                
                lastChar = c;
            }
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    private static String getStringUntilSpace(String testLine, int startIdx) {
        
        final int typeStringEndIndex = testLine.indexOf(' ', startIdx);
        
        if (typeStringEndIndex < 0) {
            throw new IllegalStateException("No whitespace char found in '" + testLine + "'");
        }

        final String string = testLine.substring(startIdx, typeStringEndIndex);
    
        return string;
    }
        
    private static TestLineType getTestLineTypeUntilSpace(String testLine, int startIdx) {
        
        final String typeString = getStringUntilSpace(testLine, startIdx);
        
        final TestLineType type = getTestLineType(typeString);
        
        return type;
    }

    private static TestLineType getTestLineType(String typeString) {
        return TestLineType.valueOf(typeString);
    }

    private static void processBlock(
            String method,
            Reader reader,
            String testLine,
            StringBuilder block,
            StringBuilder sb,
            TestRunnerUI ui) {
        
        final TestLineType type = getTestLineType(testLine.substring(TEST_BLOCK_END_MARKER.length()));
        
        processBlock(method, type, block.toString(), sb, ui);
        
        block.setLength(0);
    }
    
    private static void processBlock(String method, TestLineType type, String block, StringBuilder sb, TestRunnerUI ui) {
        
        switch (type) {
        
        case MODEL:
            final XmlTestCase model = decode(block, XmlTestCase.class);
            
            ui.setModel(model);
            break;

        case EXCEPTION:
        case ERROR:
        case FAILURE:
            final XmlThrowable xmlThrowable = decode(block, XmlThrowable.class);
            
            ui.setTestResult(method, type.getStatus(), xmlThrowable, sb.toString());
            break;
            
        default:
            throw new IllegalStateException();
        }
    }
    
    private static <T> T decode(String string, Class<T> type) {
        
        if (DEBUG) {
            System.out.println("## decode '" + string + "'");
        }
        
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(string.getBytes());

        return JAXB.unmarshal(inputStream, type);
    }

    @Override
    public String processStderr(String string, IDEComponentsFinder componentsFinder) {
        return string;
    }
}
 