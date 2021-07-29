package dev.nimbler.ide.component.common.runner;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledFileResourcePath;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.IDEComponent;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.common.types.TypeName;

public interface RunnerComponent extends IDEComponent {

    boolean isRunnable(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel,
            RunnableLanguage sourceFileLanguage);

    boolean isRunnable(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage);

    TypeName getRunnableType(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage);

    String [] getCommandLineForRunning(
        SourceFileResourcePath sourceFile,
        SourceFileModel sourceFileModel,
        TypeName entryPointType,
        String [] programArguments,
        String [] vmArguments,
        CodeAccess codeAccess);
}
