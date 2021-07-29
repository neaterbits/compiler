package dev.nimbler.ide.component.common.action;

import java.util.Objects;

import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.ui.actions.ActionExeParameters;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.component.common.ui.ComponentCompositeContext;
import dev.nimbler.ide.component.common.ui.ComponentDialogContext;

public abstract class ActionComponentExeParameters
        extends BaseActionComponentParameters
        implements ActionExeParameters {

    private final ForwardResultToCaller forwardResultToCaller;
    private final ComponentDialogContext dialogContext;
    private final ComponentCompositeContext compositeContext;
    private final ComponentIDEAccess componentIDEAccess;

    protected ActionComponentExeParameters(
            CodeAccess codeAccess,
            ForwardResultToCaller forwardResultToCaller,
            Languages languages,
            ComponentDialogContext dialogContext,
            ComponentCompositeContext compositeContext,
            ComponentIDEAccess componentIDEAccess) {

        super(codeAccess, languages);

        Objects.requireNonNull(forwardResultToCaller);
        Objects.requireNonNull(dialogContext);
        Objects.requireNonNull(compositeContext);
        Objects.requireNonNull(componentIDEAccess);
        
        this.forwardResultToCaller = forwardResultToCaller;
        this.dialogContext = dialogContext;
        this.compositeContext = compositeContext;
        this.componentIDEAccess = componentIDEAccess;
    }

    @Override
    public final ForwardResultToCaller getForwardResultToCaller() {
        return forwardResultToCaller;
    }

    public final ComponentDialogContext getDialogContext() {
        return dialogContext;
    }

    public final ComponentCompositeContext getCompositeContext() {
        return compositeContext;
    }

    public final ComponentIDEAccess getComponentIDEAccess() {
        return componentIDEAccess;
    }
}
