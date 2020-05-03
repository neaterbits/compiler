package com.neaterbits.compiler.resolver.ast.encoded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class EncodedProgram {

    private final List<EncodedModule> modules;
    
    public EncodedProgram(List<EncodedModule> modules) {
        
        Objects.requireNonNull(modules);
        
        this.modules = new ArrayList<>(modules);
    }

    public EncodedProgram(EncodedModule module) {
        this(Arrays.asList(module));
    }
    
    public EncodedProgram(EncodedParsedFile parsedFile) {
        this(new EncodedModule(null, parsedFile));
    }

    public List<EncodedModule> getModules() {
        return modules;
    }
}
