package dev.nimbler.ide.component.application.runner;

import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.build.buildsystem.common.Scope;
import dev.nimbler.build.model.BuildRoot.DependencySelector;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
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
        
        final ProjectModuleResourcePath module = sourceFile.getModule();
        
        final RuntimeEnvironment runtimeEnvironment = codeAccess.getRuntimeEnvironment(module);
        
        final DependencySelector depSelector = (scope, optional) -> scope == Scope.COMPILE;
        
        final List<CompiledModuleFileResourcePath> projectDeps
                = codeAccess.getTransitiveProjectDependenciesForProjectModule(module, depSelector).stream()
                        .map(dep -> {
                            return dep.getCompiledModuleFilePath();
                        })
                        .collect(Collectors.toList());
        
        final List<LibraryResourcePath> libraryDeps
                = codeAccess.getTransitiveLibraryDependenciesForProjectModule(module, depSelector).stream()
                        .map(LibraryDependency::getModulePath)
                        .collect(Collectors.toList());
        
        return runtimeEnvironment.getCommandLineForRunning(
                                            projectDeps,
                                            libraryDeps,
                                            entryPointType,
                                            programArguments,
                                            vmArguments);
    }
}
