package dev.nimbler.ide.common.ui.actions;

import java.util.Objects;

import dev.nimbler.ide.common.ui.translation.Translateable;

public abstract class TranslatedAction<
        APPLICABLE_PARAMETERS extends ActionAppParameters,
        EXECUTE_PARAMETERS extends ActionExeParameters>

    extends Action<APPLICABLE_PARAMETERS, EXECUTE_PARAMETERS>
    implements Translateable {

    private final String translationNamespace;
    private final String translationId;
    
    public TranslatedAction(String translationNamespace, String translationId) {
        
        Objects.requireNonNull(translationNamespace);
        Objects.requireNonNull(translationId);
        
        this.translationNamespace = translationNamespace;
        this.translationId = translationId;
    }

    @Override
    public final String getTranslationNamespace() {
        return translationNamespace;
    }

    @Override
    public final String getTranslationId() {
        return translationId;
    }
}
