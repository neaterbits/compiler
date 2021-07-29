package dev.nimbler.ide.component.runners.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jutils.swt.SWTDialogs;

import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.ide.common.ui.menus.BuiltinMenu;
import dev.nimbler.ide.common.ui.menus.MenuBuilder;
import dev.nimbler.ide.common.ui.translation.Translator;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;
import dev.nimbler.ide.component.common.action.ActionComponentExeParameters;
import dev.nimbler.ide.component.common.runner.RunnerComponent;
import dev.nimbler.ide.component.common.ui.ComponentDialogContext;
import dev.nimbler.ide.component.common.ui.DialogComponentUI;
import dev.nimbler.ide.component.common.ui.MenuComponentUI;
import dev.nimbler.ide.component.runners.RunnersComponent;
import dev.nimbler.ide.component.runners.RunnersConfigurationHelper;
import dev.nimbler.ide.component.runners.model.RunConfiguration;
import dev.nimbler.ide.component.runners.model.RunnersConfiguration;
import dev.nimbler.ide.ui.swt.SWTDialogUIContext;

public final class RunnersComponentUI implements DialogComponentUI, MenuComponentUI {

    public static final String TRANSLATION_ID_RUN = "run";

    public static final String TRANSLATION_ID_MAIN = "main";
    public static final String TRANSLATION_ID_MAIN_PROJECT = "main_project";
    public static final String TRANSLATION_ID_MAIN_CLASS = "main_class";

    public static final String TRANSLATION_ID_ARGUMENTS = "arguments";
    public static final String TRANSLATION_ID_ARGUMENTS_PROGRAM = "arguments_program";
    public static final String TRANSLATION_ID_ARGUMENTS_VM = "arguments_vm";
    
    public static final String TRANSLATION_ID_ENVIRONMENT = "environment";
    
    @Override
    public void openDialog(
            ComponentDialogContext dialogContext,
            ActionComponentExeParameters parameters) {

        final SWTDialogUIContext dialogUIContext = (SWTDialogUIContext)dialogContext;

        RunnersConfiguration runnersConfiguration = null;

        final ComponentIDEAccess componentIDEAccess = parameters.getComponentIDEAccess();

        try {
            runnersConfiguration = RunnersConfigurationHelper.readAndMergeRunnerConfiguration(componentIDEAccess);
        }
        catch (IOException ex) {
            SWTDialogs.displayError(
                    dialogUIContext.getWindow(),
                    "Error while loading runners configuration",
                    ex);
        }
        
        if (runnersConfiguration == null) {
            runnersConfiguration = new RunnersConfiguration();
            runnersConfiguration.setRunConfigurations(new ArrayList<>());
        }
        
        final Function<RunConfiguration, String> getMainClassText
            = runConfiguration -> {
              
                final CompileableLanguage language
                    = componentIDEAccess.getLanguages().getSourceFileLanguage(
                                                                CompileableLanguage.class,
                                                                runConfiguration.getSourceFile());
                
                return language.getCompleteNameString(runConfiguration.getMainClass());
            };

        final List<RunnerComponent> runnerComponents
            = parameters.getComponentIDEAccess().findComponents(RunnerComponent.class);
        
        final SWTRunnableDialog runnableDialog = new SWTRunnableDialog(
                dialogUIContext.getWindow(),
                runnersConfiguration.getRunConfigurations(),
                runnerComponents,
                parameters,
                getMainClassText);

        runnableDialog.open();

        final List<RunConfiguration> updated = runnableDialog.getRunConfigurations();
        
        if (runnersConfiguration.getRunConfigurations() == updated) {
            throw new IllegalStateException();
        }

        try {
            RunnersConfigurationHelper.splitAndSaveRunnerConfiguration(componentIDEAccess, updated);
        } catch (IOException ex) {
            SWTDialogs.displayError(
                    dialogUIContext.getWindow(),
                    "Error while updating runners configuration",
                        ex);
        }
    }
    
    public static final String TRANSLATION_ID_RUN_AS = "run_as";
    
    public static final String TRANSLATION_ID_RUN_CONFIGURATIONS = "run_configurations";

    @Override
    public void addToMenu(IDEComponentsConstAccess componentsAccess, MenuBuilder menuBuilder) {

        final List<RunnerComponent> runnerComponents
            = componentsAccess.findComponents(RunnerComponent.class);

        menuBuilder.addToMenu(
                BuiltinMenu.RUN,
                runMenuBuilder ->
                    runMenuBuilder.addSubMenu(
                            Translator.getComponentNamespace(RunnersComponent.class),
                            TRANSLATION_ID_RUN_AS,
                            b -> {
                        for (RunnerComponent runnerComponent : runnerComponents) {
                            b.addAction(new RunnerAction(runnerComponent));
                        }
                    })
                    .addAction(
                            new RunConfigurationsAction(
                                    RunnersComponent.class,
                                    TRANSLATION_ID_RUN_CONFIGURATIONS)));
    }
}
