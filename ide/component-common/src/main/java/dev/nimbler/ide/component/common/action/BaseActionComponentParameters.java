package dev.nimbler.ide.component.common.action;

import java.util.Objects;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.ui.actions.BaseActionParameters;
import dev.nimbler.ide.component.common.language.Languages;

public abstract class BaseActionComponentParameters
    extends BaseActionParameters {

    private final Languages languages;

    protected BaseActionComponentParameters(BuildRoot buildRoot, Languages languages) {
        super(buildRoot);

        Objects.requireNonNull(languages);
        
        this.languages = languages;
    }

    @Override
    public final <T> T getSourceFileLanguage(
            Class<T> languageInterface,
            SourceFileResourcePath sourceFileResourcePath) {

        return languages.getSourceFileLanguage(languageInterface, sourceFileResourcePath);
    }
}
