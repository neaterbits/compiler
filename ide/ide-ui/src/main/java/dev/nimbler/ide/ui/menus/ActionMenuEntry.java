package dev.nimbler.ide.ui.menus;

import dev.nimbler.ide.common.ui.actions.ActionAppParameters;
import dev.nimbler.ide.common.ui.actions.ActionExeParameters;
import dev.nimbler.ide.common.ui.actions.TranslatedAction;
import dev.nimbler.ide.common.ui.keys.KeyBindings;

public final class ActionMenuEntry
        extends BaseActionMenuEntry<
                    ActionAppParameters,
                    ActionExeParameters,
                    TranslatedAction<ActionAppParameters, ActionExeParameters>> {
    

    @SuppressWarnings("unchecked")
    public ActionMenuEntry(TranslatedAction<?, ?> action, KeyBindings keyBindings) {
        super((TranslatedAction<ActionAppParameters, ActionExeParameters>)action, keyBindings);
    }

    @Override
    public String getTranslationNamespace() {
        return action.getTranslationNamespace();
    }

    @Override
    public String getTranslationId() {
        return action.getTranslationId();
    }
}
