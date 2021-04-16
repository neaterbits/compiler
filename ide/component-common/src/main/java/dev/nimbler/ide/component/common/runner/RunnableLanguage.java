package dev.nimbler.ide.component.common.runner;


import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.common.types.TypeName;

public interface RunnableLanguage {

    boolean isSourceFileRunnable(SourceFileResourcePath sourceFile, SourceFileModel sourceFileModel);
    
    TypeName getRunnableType(ClassBytecode bytecode);

    boolean isBytecodeRunnable(ClassBytecode bytecode);
}
