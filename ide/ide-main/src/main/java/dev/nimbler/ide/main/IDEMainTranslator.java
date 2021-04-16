package dev.nimbler.ide.main;

import dev.nimbler.ide.common.ui.translation.Translateable;
import dev.nimbler.ide.common.ui.translation.Translator;
import dev.nimbler.ide.component.build.ui.BuildIssuesComponent;
import dev.nimbler.ide.component.common.ui.ComponentUI;
import dev.nimbler.ide.component.common.ui.DetailsComponentUI;
import dev.nimbler.ide.component.compiledfiledebug.ui.CompiledFileViewComponent;
import dev.nimbler.ide.component.console.output.ui.ConsoleOutputComponent;
import dev.nimbler.ide.component.tasks.ui.TasksUIComponent;
import dev.nimbler.ide.ui.controller.IDECoreTranslator;

final class IDEMainTranslator implements Translator {

    private final IDECoreTranslator coreTranslator;
    
    IDEMainTranslator() {

        this.coreTranslator = new IDECoreTranslator();
    }
    
    private static boolean isComponentNamespace(Translateable translateable, Class<? extends ComponentUI> component) {
        
        return translateable.getTranslationNamespace()
                .equals(Translator.getComponentNamespace(component));
    }

    @Override
    public String translate(Translateable translateable) {

        final String translation;
        
        if (isComponentNamespace(translateable, BuildIssuesComponent.class)) {
            
            if (translateable.getTranslationId().equals(DetailsComponentUI.TITLE_TRANSLATION_ID)) {
                translation = "Build Issues";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else if (isComponentNamespace(translateable, CompiledFileViewComponent.class)) {

            if (translateable.getTranslationId().equals(DetailsComponentUI.TITLE_TRANSLATION_ID)) {
                translation = "Compiled File";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else if (isComponentNamespace(translateable, TasksUIComponent.class)) {
            if (translateable.getTranslationId().equals(DetailsComponentUI.TITLE_TRANSLATION_ID)) {
                translation = "Tasks";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else if (isComponentNamespace(translateable, ConsoleOutputComponent.class)) {
            if (translateable.getTranslationId().equals(DetailsComponentUI.TITLE_TRANSLATION_ID)) {
                translation = "Console";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else {
            translation = coreTranslator.translate(translateable);
        }
        
        return translation;
    }
}
