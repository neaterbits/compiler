package dev.nimbler.build.model.runtimeenvironment;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
import dev.nimbler.language.bytecode.common.BytecodeFormat;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.DependencyFile;
import dev.nimbler.language.common.typesources.libs.ClassLibs;

public interface RuntimeEnvironment {

    ClassLibs getSystemLibraries();
    
    BytecodeFormat getBytecodeFormat();

    Set<TypeName> getTypesFromCompiledModuleFile(CompiledModuleFileResourcePath compiledModuleFileResourcePath) throws IOException;

    Set<TypeName> getTypesFromLibraryFile(LibraryResourcePath libraryResourcePath) throws IOException;

    Set<TypeName> getTypesFromSystemLibraryFile(DependencyFile systemLibraryPath) throws IOException;

    boolean canReadCodeMapFromCompiledCode();
    
    String[] getCommandLineForRunning(
            List<CompiledModuleFileResourcePath> projects,
            List<LibraryResourcePath> libraries,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments);

}
