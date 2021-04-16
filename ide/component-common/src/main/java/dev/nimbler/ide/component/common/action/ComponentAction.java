package dev.nimbler.ide.component.common.action;

import dev.nimbler.ide.common.ui.actions.TranslatedAction;
import dev.nimbler.ide.common.ui.translation.Translator;
import dev.nimbler.ide.component.common.IDEComponent;

public abstract class ComponentAction
    extends TranslatedAction<ActionComponentAppParameters, ActionComponentExeParameters> {

    protected ComponentAction(Class<? extends IDEComponent> componentType, String translationId) {
        super(Translator.getComponentNamespace(componentType), translationId);
    
        System.out.println("## component action " + translationId);
    }
}
