package dev.nimbler.ide.component.runner.test.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import org.jutils.swt.TreeContentAdapter;

import dev.nimbler.ide.common.ui.translation.Translateable;
import dev.nimbler.ide.common.ui.translation.Translator;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlStackTraceEntry;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlTestCase;
import dev.nimbler.ide.component.common.runner.model.throwable.XmlThrowable;
import dev.nimbler.ide.component.runner.test.TestRunnerComponent;
import dev.nimbler.ide.component.runner.test.TestStatus;

public class SWTTestComposite extends Composite {

    private static final Object [] NO_ELEMENTS = new Object[0];

    private ProgressBar progressBar;

    private TreeViewer treeViewer;
    
    private ListViewer stacktraceList;
    
    private StyledText outputText;

    private final List<TestMethodContent> testMethods;
    
    public SWTTestComposite(Composite composite, int style, Translator translator) {
        super(composite, style);
        
        this.testMethods = new ArrayList<>();

        final SashForm sashForm = new SashForm(composite, SWT.VERTICAL);

        final Composite labelComposite = new Composite(sashForm, SWT.NONE);
        
        labelComposite.setLayout(new GridLayout(3, true));

        this.progressBar = new ProgressBar(labelComposite, SWT.BORDER);
        
        this.treeViewer = new TreeViewer(labelComposite, SWT.BORDER);
        
        initTreeViewer(treeViewer);
        
        final TabFolder outputTabFolder = new TabFolder(sashForm, SWT.NONE);
        
        final TabItem stacktraceTabItem = new TabItem(outputTabFolder, SWT.NONE);
        
        final Translateable exceptionTitle = Translateable.fromComponent(
                TestRunnerUI.EXCEPTION,
                TestRunnerComponent.class);
        
        stacktraceTabItem.setText(translator.translate(exceptionTitle));
        
        this.stacktraceList = new ListViewer(outputTabFolder, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
        stacktraceTabItem.setControl(stacktraceList.getControl());
        
        initStackTraceList(stacktraceList, translator);

        final TabItem outputTabItem = new TabItem(outputTabFolder, SWT.NONE);

        final Translateable outputTitle = Translateable.fromComponent(
                TestRunnerUI.OUTPUT,
                TestRunnerComponent.class);

        outputTabItem.setText(translator.translate(outputTitle));
        
        this.outputText = new StyledText(outputTabFolder, SWT.BORDER|SWT.READ_ONLY|SWT.H_SCROLL|SWT.V_SCROLL);
        
        outputTabItem.setControl(outputText);
    }
    
    private void initTreeViewer(TreeViewer treeViewer) {
        
        treeViewer.setContentProvider(new TreeContentAdapter() {
            
            @Override
            public Object getParent(Object obj) {

                return obj instanceof TestMethodContent
                        ? treeViewer.getInput()
                        : null;
            }
            
            @Override
            public Object[] getElements(Object obj) {

                final Object [] elements;
                
                if (obj instanceof XmlTestCase) {
                    final XmlTestCase xmlTestCase = (XmlTestCase)obj;
                    
                    elements = xmlTestCase.getTestMehods().toArray(
                                        new XmlTestCase[xmlTestCase.getTestMehods().size()]);
                }
                else if (obj instanceof TestMethodContent) {
                    elements = NO_ELEMENTS;
                }
                else {
                    throw new IllegalStateException();
                }
                
                return elements;
            }
        });
    
        treeViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object obj) {
                
                final String label;
                
                if (obj instanceof XmlTestCase) {
                    final XmlTestCase xmlTestCase = (XmlTestCase)obj;
                    
                    label = xmlTestCase.getTestClass();
                }
                else if (obj instanceof TestMethodContent) {
                    
                    final TestMethodContent testMethodContent = (TestMethodContent)obj;
                    
                    if (testMethodContent.result == null) {
                        label = testMethodContent.method;
                    }
                    else {
                        label = testMethodContent.result.status.name() + ' ' + testMethodContent.method;
                    }
                }
                else {
                    throw new IllegalStateException();
                }
                
                return label;
            }
        });
        
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {

                final Object selection = event.getStructuredSelection().getFirstElement();

                final XmlThrowable throwable;
                final String output;
                
                if (selection instanceof XmlThrowable) {
                    throwable = null;
                    output = "";
                }
                else if (selection instanceof TestMethodContent) {

                    final TestMethodContent testMethodContent = (TestMethodContent)selection;
                    
                    if (testMethodContent.result != null) {
                        throwable = testMethodContent.result.throwable;
                        output = testMethodContent.result.output != null
                                ? testMethodContent.result.output
                                : "";
                    }
                    else {
                        throwable = null;
                        output = "";
                    }
                }
                else {
                    throw new IllegalStateException();
                }

                stacktraceList.setInput(throwable);
                outputText.setText(output);
            }
        });
    }
    
    private static void initStackTraceList(ListViewer listViewer, Translator translator) {
     
        listViewer.setContentProvider(new IStructuredContentProvider() {
            
            @Override
            public Object[] getElements(Object obj) {
                
                final List<XmlStackTraceEntry> entries = new ArrayList<>();
                
                XmlThrowable throwable = (XmlThrowable)obj;

                for (;;) {
                    
                    for (XmlStackTraceEntry entry : throwable.getStackTrace().getEntries()) {

                        entries.add(entry);
                        
                    }

                    throwable = throwable.getCause();
                    
                    if (throwable == null) {
                        break;
                    }
                    
                    entries.add(null);
                }
                
                return entries.toArray(new XmlStackTraceEntry[entries.size()]);
            }
        });
        
        listViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object obj) {
                
                final String label;
                
                if (obj == null) {
                    label = translator.translate(Translateable.fromComponent(
                                                            TestRunnerUI.CAUSED_BY,
                                                            TestRunnerComponent.class));
                }
                else if (obj instanceof XmlStackTraceEntry) {
                    
                    final XmlStackTraceEntry entry = (XmlStackTraceEntry)obj;
                    
                    label = entry.getCl() + '.' + entry.getMethod();
                }
                else {
                    throw new IllegalStateException();
                }

                return label;
            }
        });
    }

    public void setModel(XmlTestCase testCase) {
    
        Objects.requireNonNull(testCase);
        
        testMethods.clear();

        for (String method : testCase.getTestMehods()) {
            testMethods.add(new TestMethodContent(method));
        }
        
        progressBar.setMinimum(0);
        progressBar.setMaximum(testCase.getTestMehods().size());

        treeViewer.setInput(testCase);
    }

    public void setTestResult(
            String method,
            TestStatus status,
            XmlThrowable throwable,
            String output) {

        Objects.requireNonNull(method);
        Objects.requireNonNull(status);
        
        final XmlTestCase xmlTestCase = (XmlTestCase)treeViewer.getInput();

        final int indexOf = xmlTestCase.getTestMehods().indexOf(method);

        final TestMethodContent testMethodContent = testMethods.get(indexOf);

        testMethodContent.result = new TestResult(status, throwable, output);
        
        progressBar.setSelection((int)testMethods.stream()
                .filter(m -> m.result != null)
                .count());
        
        treeViewer.refresh(testMethodContent);
    }
    
    private static class TestMethodContent {
        
        private final String method;
        
        private TestResult result;

        TestMethodContent(String method) {
            this.method = method;
        }
    }
    
    private static class TestResult {
        
        private final TestStatus status;
        private final XmlThrowable throwable;
        private final String output;

        TestResult(TestStatus status, XmlThrowable throwable, String output) {
            this.status = status;
            this.throwable = throwable;
            this.output = output;
        }
    }
}
