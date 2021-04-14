package dev.nimbler.ide.component.common.runner;


import dev.nimbler.build.types.resource.SourceFileHolderResourcePath;
import dev.nimbler.ide.common.model.source.SourceFileModel;

public interface RunnableLanguage {

    boolean isSourceFileRunnable(SourceFileHolderResourcePath sourceFile, SourceFileModel sourceFileModel);
    
}
