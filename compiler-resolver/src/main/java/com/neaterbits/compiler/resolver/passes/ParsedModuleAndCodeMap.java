package com.neaterbits.compiler.resolver.passes;

import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.strategies.compilemodules.ParsedModule;
import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.build.strategies.compilemodules.PossibleTypeRefs;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class ParsedModuleAndCodeMap<PARSED_FILE extends ParsedFile>
                extends ParsedModule<PARSED_FILE> {
                    
    private final CompilerCodeMap codeMap;

    public ParsedModuleAndCodeMap(ParsedModule<PARSED_FILE> parsedModule, CompilerCodeMap codeMap) {
        
        super(parsedModule);

        this.codeMap = codeMap;
    }

    public ParsedModuleAndCodeMap(List<PARSED_FILE> parsedFiles, int fileNo, CompilerCodeMap codeMap) {
        super(parsedFiles.stream()
                .map(f -> new ParsedWithCachedRefs<>(f, new PossibleTypeRefs(), fileNo))
                .collect(Collectors.toList()));
        
        this.codeMap = codeMap;
    }

    public CompilerCodeMap getCodeMap() {
        return codeMap;
    }
}
