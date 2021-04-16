package dev.nimbler.ide.component.common.ui;

import dev.nimbler.ide.component.common.action.ActionComponentExeParameters;

public interface DialogComponentUI extends ComponentUI {

    void openDialog(
            ComponentDialogContext dialogContext,
            ActionComponentExeParameters parameters);

}
