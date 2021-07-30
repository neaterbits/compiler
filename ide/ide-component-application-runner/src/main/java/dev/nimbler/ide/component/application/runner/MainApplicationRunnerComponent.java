package dev.nimbler.ide.component.application.runner;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledFileResourcePath;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.runner.RunnableLanguage;
import dev.nimbler.ide.component.common.runner.RunnerComponent;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.common.types.TypeName;

public final class MainApplicationRunnerComponent implements RunnerComponent {

    @Override
    public boolean isRunnable(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel,
            RunnableLanguage sourceFileLanguage) {

        return sourceFileLanguage.isSourceFileRunnable(sourceFile, sourceFileModel);
    }

    @Override
    public TypeName getRunnableType(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage) {

        return sourceFileLanguage.getRunnableType(bytecode);
    }

    @Override
    public boolean isRunnable(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage) {
        
        return sourceFileLanguage.isBytecodeRunnable(bytecode);
    }

    @Override
    public String[] getCommandLineForRunning(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments,
            CodeAccess codeAccess) {
        
        return RunnerComponent.getCommandLineFromArguments(
                sourceFile,
                sourceFileModel,
                entryPointType,
                programArguments,
                vmArguments,
                codeAccess);
    }
}
