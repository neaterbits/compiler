package dev.nimbler.ide.component.runners.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import dev.nimbler.ide.common.ui.actions.ActionExeParameters;
import dev.nimbler.ide.common.ui.translation.Translateable;
import dev.nimbler.ide.common.ui.translation.Translator;
import dev.nimbler.ide.component.common.action.ActionComponentExeParameters;
import dev.nimbler.ide.component.common.runner.RunnerComponent;
import dev.nimbler.ide.component.runners.RunnerHelper;
import dev.nimbler.ide.component.runners.RunnersComponent;
import dev.nimbler.ide.component.runners.model.RunConfiguration;

final class SWTRunnableDialog extends Dialog {
    
    private final Translator translator;
    
    private final List<RunConfiguration> runConfigurations;
    
    private final List<RunnerComponent> runnerComponents;
    
    private final ActionExeParameters actionExeParameters;

    private final Function<RunConfiguration, String> getMainClassText;
    
    private ListViewer runnableListViewer;
    
    private Text projectText;
    
    private Text mainClassText;
    
    private Text programArguments;
    
    private Text vmArguments;
    
    public SWTRunnableDialog(
            Shell shell,
            List<RunConfiguration> runConfigurations,
            List<RunnerComponent> runnerComponents,
            ActionComponentExeParameters actionExeParameters,
            Function<RunConfiguration, String> getMainClassText) {
        
        super(shell);
        
        Objects.requireNonNull(runConfigurations);
        Objects.requireNonNull(runnerComponents);
        Objects.requireNonNull(actionExeParameters);
        Objects.requireNonNull(getMainClassText);

        this.translator = actionExeParameters.getComponentIDEAccess().getTranslator();
        this.runConfigurations = new ArrayList<>(runConfigurations);
        this.runnerComponents = runnerComponents;
        this.actionExeParameters = actionExeParameters;
        this.getMainClassText = getMainClassText;
    }

    @Override
    protected void configureShell(Shell window) {

        super.configureShell(window);

        window.setText(translate(RunnersComponentUI.TRANSLATION_ID_RUN_CONFIGURATIONS));
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        
        final Composite control = (Composite)super.createDialogArea(parent);
        
        control.setLayout(new FillLayout());
        
        final SashForm sashForm = new SashForm(control, SWT.NONE);
        
        this.runnableListViewer = makeRunnableList(sashForm);
        
        runnableListViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                
                final RunConfiguration runConfiguration = (RunConfiguration)element;
                
                return runConfiguration.getName();
            }
        });
        
        runnableListViewer.setContentProvider(new IStructuredContentProvider() {
            
            @Override
            public Object[] getElements(Object model) {
                
                final RunConfiguration [] elements
                    = runConfigurations.toArray(new RunConfiguration[runConfigurations.size()]);
                
                return elements;
            }
        });
        
        runnableListViewer.setInput(runConfigurations);

        makeRunnableConfigurationComposite(sashForm);
        
        runnableListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {

                final RunConfiguration runConfiguration
                    = (RunConfiguration)event.getStructuredSelection().getFirstElement();
                
                final String mainClassString = getMainClassText.apply(runConfiguration);
                
                setText(projectText, runConfiguration.getProject().getModuleId().getId());

                mainClassText.setText(mainClassString);
                
                setText(programArguments, runConfiguration.getProgramArguments());

                setText(vmArguments, runConfiguration.getVmArguments());
            }
        });
        
        sashForm.setWeights(new int [] { 1, 4 });
        
        if (!runConfigurations.isEmpty()) {
            runnableListViewer.setSelection(new StructuredSelection(runConfigurations.get(0)));
        }

        return control; 
    }
    
    private static void setText(Text text, String string) {
        text.setText(string != null ? string : "");
    }
    
    private ListViewer makeRunnableList(SashForm sashForm) {
        
        final Composite runnableListComposite = new Composite(sashForm, SWT.NONE);
        
        runnableListComposite.setLayout(new FillLayout());

        final ListViewer listViewer = new ListViewer(runnableListComposite, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
        
        return listViewer;
    }
    
    private Composite makeRunnableConfigurationComposite(SashForm sashForm) {

        final TabFolder tabFolder = new TabFolder(sashForm, SWT.NONE);
        
        tabFolder.setLayout(new FillLayout());

        makeMainTab(tabFolder);

        makeArgumentsTab(tabFolder);
        // makeEnvironmentTab(tabFolder);
        
        return tabFolder;
    }
    
    private void makeMainTab(TabFolder tabFolder) {
        
        final Composite mainComposite = new Composite(tabFolder, SWT.NONE);

        addTabItem(tabFolder, mainComposite, RunnersComponentUI.TRANSLATION_ID_MAIN);

        mainComposite.setLayout(new GridLayout(1, false));
        
        final Group projectGroup = new Group(mainComposite, SWT.NONE);
        
        projectGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        projectGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
        projectGroup.setText(translate(RunnersComponentUI.TRANSLATION_ID_MAIN_PROJECT));
        
        this.projectText = new Text(projectGroup, SWT.BORDER);
        projectText.setEditable(false);
        
        final Group mainClassGroup = new Group(mainComposite, SWT.NONE);
        mainClassGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        mainClassGroup.setLayout(new FillLayout());
        mainClassGroup.setText(translate(RunnersComponentUI.TRANSLATION_ID_MAIN_CLASS));
        
        this.mainClassText = new Text(mainClassGroup, SWT.BORDER);
    }
    
    private void makeArgumentsTab(TabFolder tabFolder) {
        
        final Composite argumentsComposite = new Composite(tabFolder, SWT.NONE);

        argumentsComposite.setLayout(new GridLayout(1, false));

        addTabItem(tabFolder, argumentsComposite, RunnersComponentUI.TRANSLATION_ID_ARGUMENTS);

        final Group programArgumentsGroup = new Group(argumentsComposite, SWT.BORDER);
        
        programArgumentsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        programArgumentsGroup.setLayout(new FillLayout());
        programArgumentsGroup.setText(translate(RunnersComponentUI.TRANSLATION_ID_ARGUMENTS_PROGRAM));
        
        this.programArguments = new Text(programArgumentsGroup, SWT.BORDER);
        
        final Group vmArgumentsGroup = new Group(argumentsComposite, SWT.NONE);
        vmArgumentsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        vmArgumentsGroup.setLayout(new FillLayout());
        vmArgumentsGroup.setText(translate(RunnersComponentUI.TRANSLATION_ID_ARGUMENTS_VM));
        
        this.vmArguments = new Text(vmArgumentsGroup, SWT.BORDER);
    }
    
    /*
    private void makeEnvironmentTab(TabFolder tabFolder) {
        
        final Composite mainComposite = new Composite(tabFolder, SWT.NONE);

        addTabItem(tabFolder, mainComposite, TRANSLATION_ID_ENVIRONMENT);
    }
    */
    
    private void addTabItem(TabFolder tabFolder, Composite composite, String translationId) {
        
        final TabItem arguments = new TabItem(tabFolder, SWT.NONE);

        arguments.setText(translate(translationId));
        
        arguments.setControl(composite);
    }
    
    private String translate(String translationId) {
        
        final Translateable translateable = Translateable.fromComponent(
                translationId,
                RunnersComponent.class);

        return translator.translate(translateable);
    }

    @Override
    protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
        
        final String updatedLabel;
        
        if (id == IDialogConstants.OK_ID) {
            updatedLabel = translate(RunnersComponentUI.TRANSLATION_ID_RUN);
        }
        else {
            updatedLabel = label;
        }
        
        return super.createButton(parent, id, updatedLabel, defaultButton);
    }

    @Override
    protected Point getInitialSize() {

        return new Point(800, 650);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
        
        final IStructuredSelection selection = runnableListViewer.getStructuredSelection();
        
        final RunConfiguration runConfiguration = (RunConfiguration)selection.getFirstElement();

        final RunnerComponent runnerComponent
            = runnerComponents.stream()
            .filter(c -> c.getClass().getName().equals(runConfiguration.getRunnerComponent()))
            .findFirst()
            .orElse(null);

        RunnerHelper.run(runnerComponent, runConfiguration, actionExeParameters);
    }

    public List<RunConfiguration> getRunConfigurations() {
        return runConfigurations;
    }
}
