package com.neaterbits.ide.component.common.runner;


import com.neaterbits.build.types.resource.SourceFileHolderResourcePath;
import com.neaterbits.ide.common.model.source.SourceFileModel;

public interface RunnableLanguage {

    boolean isSourceFileRunnable(SourceFileHolderResourcePath sourceFile, SourceFileModel sourceFileModel);
    
}
