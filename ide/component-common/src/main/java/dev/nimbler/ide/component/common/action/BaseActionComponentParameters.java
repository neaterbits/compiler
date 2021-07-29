package dev.nimbler.ide.component.common.action;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.ui.actions.BaseActionParameters;
import dev.nimbler.ide.component.common.language.Languages;

public abstract class BaseActionComponentParameters
    extends BaseActionParameters {

    private final CodeAccess codeAccess;
    private final Languages languages;

    protected BaseActionComponentParameters(CodeAccess codeAccess, Languages languages) {

        Objects.requireNonNull(codeAccess);
        
        Objects.requireNonNull(languages);
        
        this.codeAccess = codeAccess;
        this.languages = languages;
    }

    @Override
    public final <T> T getSourceFileLanguage(
            Class<T> languageInterface,
            SourceFileResourcePath sourceFileResourcePath) {

        return languages.getSourceFileLanguage(languageInterface, sourceFileResourcePath);
    }

    public final CodeAccess getCodeAccess() {
        return codeAccess;
    }
}
