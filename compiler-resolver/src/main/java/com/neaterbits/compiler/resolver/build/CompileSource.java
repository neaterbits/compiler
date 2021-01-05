package com.neaterbits.compiler.resolver.build;

import java.util.Objects;

public final class CompileSource {

    private final String source;
    private final String fileName;
    
    public CompileSource(String source, String fileName) {

        Objects.requireNonNull(source);
        Objects.requireNonNull(fileName);
        
        this.source = source;
        this.fileName = fileName;
    }

    public String getSource() {
        return source;
    }

    public String getFileName() {
        return fileName;
    }
}
