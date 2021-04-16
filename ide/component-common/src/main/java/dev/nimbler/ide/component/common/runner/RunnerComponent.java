package dev.nimbler.ide.component.common.runner;

import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.IDEComponent;
import dev.nimbler.ide.component.common.IDEComponentsFinder;
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
            BuildRoot buildRoot);
    
    default void startOutput() {
        
    }

    default String processStdout(String string, IDEComponentsFinder componentsFinder) {
        return string;
    }

    default String processStderr(String string, IDEComponentsFinder componentsFinder) {
        return string;
    }

    public static String [] getCommandLineFromArguments(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments,
            BuildRoot buildRoot) {
        
        final ProjectModuleResourcePath module = sourceFile.getModule();
        
        final RuntimeEnvironment runtimeEnvironment = buildRoot.getRuntimeEnvironment(module);
        
        final List<CompiledModuleFileResourcePath> projectDeps
                = buildRoot.getProjectDependenciesForProjectModule(module).stream()
                        .map(dep -> {
                            final ProjectModuleResourcePath depPath = dep.getModulePath();
                            
                            return buildRoot.getCompiledModuleFile(depPath);
                        })
                        .collect(Collectors.toList());
        
        final List<LibraryResourcePath> libraryDeps
                = buildRoot.getLibraryDependenciesForProjectModule(module).stream()
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
