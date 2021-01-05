package com.neaterbits.compiler.resolver.build;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.BaseResolvedModule;
import com.neaterbits.build.strategies.compilemodules.ParsedModule;
import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class ResolvedSourceModule<PARSED_FILE extends ParsedFile>
    extends BaseResolvedModule<PARSED_FILE, ResolveError> {

    private final SourceModule sourceModule;
        
    ResolvedSourceModule(
            SourceModule sourceModule,
            ParsedModule<PARSED_FILE, ResolveError> parsedModule,
            List<ResolveError> resolveErrors) {

        super(parsedModule, resolveErrors);
        
        this.sourceModule = sourceModule;
    }

    public SourceModule getSourceModule() {
        return sourceModule;
    }
    
    public CompileSource getCompileSource(String name) {
        
        return sourceModule.getCompileSource(name);
    }
    
    public ParsedWithCachedRefs<PARSED_FILE, ResolveError> getParsed(FileSpec fileSpec) {
        
        Objects.requireNonNull(fileSpec);
        
        return getParsedModule().getParsed().stream()
                .filter(p -> p.getParsedFile().getFileSpec().equals(fileSpec))
                .findFirst()
                .get();
                
    }
}
