package dev.nimbler.ide.component.common.runtimeenvironment;

import java.util.List;

import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
import dev.nimbler.ide.common.codeaccess.types.LanguageName;
import dev.nimbler.language.common.types.TypeName;

public interface RuntimeEnvironmentComponent {

    List<LanguageName> getSupportedLanguages();

    String [] getCommandLineForRunning(
            List<CompiledModuleFileResourcePath> projects,
            List<LibraryResourcePath> libraries,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments);

}
