package dev.nimbler.ide.common.ui.actions;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.model.source.SourceFileModel;

public interface ActionSourceFileParameters {

    SourceFileResourcePath getCurrentSourceFileResourcePath();

    SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath);
    
    <T> T getSourceFileLanguage(Class<T> languageInterface, SourceFileResourcePath sourceFile);

}
