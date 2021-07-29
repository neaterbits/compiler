package dev.nimbler.ide.component.java.runtimeenvironment;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dev.nimbler.build.language.java.jdk.JavaRuntimeEnvironment;
import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
import dev.nimbler.ide.common.codeaccess.types.LanguageName;
import dev.nimbler.ide.component.common.runtimeenvironment.RuntimeEnvironmentComponent;
import dev.nimbler.ide.component.java.language.JavaLanguageComponent;
import dev.nimbler.language.common.types.TypeName;

public final class JavaRuntimeEnvironmentComponent implements RuntimeEnvironmentComponent {
    
    private final JavaRuntimeEnvironment runtimeEnvironment;

    public JavaRuntimeEnvironmentComponent(JavaRuntimeEnvironment runtimeEnvironment) {
        
        Objects.requireNonNull(runtimeEnvironment);

        this.runtimeEnvironment = runtimeEnvironment;
    }

    @Override
    public String[] getCommandLineForRunning(
            List<CompiledModuleFileResourcePath> projects,
            List<LibraryResourcePath> libraries,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments) {
        
        return runtimeEnvironment.getCommandLineForRunning(
                                        projects,
                                        libraries,
                                        entryPointType,
                                        programArguments,
                                        vmArguments);
    }

    @Override
    public List<LanguageName> getSupportedLanguages() {
        
        return 
                Arrays.asList(
                        JavaLanguageComponent.LANGUAGE_NAME);
    }
}
