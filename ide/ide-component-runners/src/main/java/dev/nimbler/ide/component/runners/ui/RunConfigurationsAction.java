package dev.nimbler.ide.component.runners.ui;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.component.common.IDEComponent;
import dev.nimbler.ide.component.common.action.ActionComponentAppParameters;
import dev.nimbler.ide.component.common.action.ActionComponentExeParameters;
import dev.nimbler.ide.component.common.action.ComponentAction;

final class RunConfigurationsAction extends ComponentAction {

    public RunConfigurationsAction(
            Class<? extends IDEComponent> componentType,
            String translationId) {

        super(componentType, translationId);
    }

    @Override
    public void execute(ActionComponentExeParameters parameters) {

        final RunnersComponentUI runnersComponentUI = new RunnersComponentUI();
        
        runnersComponentUI.openDialog(parameters.getDialogContext(), parameters);
    }

    @Override
    public boolean isApplicableInContexts(
            ActionComponentAppParameters parameters,
            ActionContexts focusedContexts,
            ActionContexts allContexts) {

        return true;
    }
}
