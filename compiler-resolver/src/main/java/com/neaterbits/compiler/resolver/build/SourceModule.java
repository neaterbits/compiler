package com.neaterbits.compiler.resolver.build;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.BaseCompileModule;
import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.dependencies.ProjectDependency;

final class SourceModule extends BaseCompileModule {

    private final String source;
    private final String fileName;
    
    public SourceModule(
            String source,
            String fileName,
            Charset charset,
            List<ProjectDependency> projectDependencies,
            List<LibraryDependency> libraryDependencies) {
    
        super(charset, projectDependencies, libraryDependencies);
    
        Objects.requireNonNull(source);
        
        this.source = source;
        this.fileName = fileName;
    }

    String getSource() {
        return source;
    }

    String getFileName() {
        return fileName;
    }
}
