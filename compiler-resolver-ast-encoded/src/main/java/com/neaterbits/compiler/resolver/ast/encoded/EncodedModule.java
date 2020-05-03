package com.neaterbits.compiler.resolver.ast.encoded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.modules.ModuleSpec;

public final class EncodedModule {

    private final ModuleSpec moduleSpec;
    private final List<EncodedParsedFile> parsedFiles;
    
    public EncodedModule(ModuleSpec moduleSpec, Collection<EncodedParsedFile> parsedFiles) {

        Objects.requireNonNull(parsedFiles);
        
        this.moduleSpec = moduleSpec;
        this.parsedFiles = new ArrayList<>(parsedFiles);
    }
    
    public EncodedModule(ModuleSpec moduleSpec, EncodedParsedFile parsedFiles) {
        this(moduleSpec, Arrays.asList(parsedFiles));
    }

    public ModuleSpec getModuleSpec() {
        return moduleSpec;
    }

    public List<EncodedParsedFile> getParsedFiles() {
        return parsedFiles;
    }

}
