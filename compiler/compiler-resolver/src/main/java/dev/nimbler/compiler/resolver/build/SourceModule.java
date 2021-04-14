package dev.nimbler.compiler.resolver.build;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.coll.Coll;

import dev.nimbler.build.strategies.compilemodules.BaseCompileModule;
import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.dependencies.ProjectDependency;

public final class SourceModule extends BaseCompileModule {

    private final List<CompileSource> sources;
    
    public SourceModule(
            List<CompileSource> sources,
            Charset charset,
            List<ProjectDependency> projectDependencies,
            List<LibraryDependency> libraryDependencies) {
    
        super(charset, projectDependencies, libraryDependencies);
    
        Objects.requireNonNull(sources);
        
        this.sources = Coll.immutable(sources);
    }

    public List<CompileSource> getSources() {
        return sources;
    }

    public CompileSource getCompileSource(String name) {
        
        Objects.requireNonNull(name);
        
        return sources.stream()
                .filter(source -> source.getFileName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
