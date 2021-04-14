package dev.nimbler.compiler.resolver.passes;

import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.build.strategies.compilemodules.ParsedModule;
import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.build.strategies.compilemodules.PossibleTypeRefs;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypesMap;

public final class ParsedModuleAndCodeMap<PARSED_FILE extends ParsedFile>
                extends ParsedModule<PARSED_FILE, ResolveError> {
                    
    private final CompilerCodeMap codeMap;
    private final TypesMap<TypeName> typesMap;

    public ParsedModuleAndCodeMap(ParsedModule<PARSED_FILE, ResolveError> parsedModule, CompilerCodeMap codeMap) {
        
        super(parsedModule);

        this.codeMap = codeMap;
        this.typesMap = codeMap.makeTypesMap();
    }

    public ParsedModuleAndCodeMap(List<PARSED_FILE> parsedFiles, int fileNo, CompilerCodeMap codeMap) {
        super(parsedFiles.stream()
                .map(f -> new ParsedWithCachedRefs<PARSED_FILE, ResolveError>(f, new PossibleTypeRefs(), fileNo))
                .collect(Collectors.toList()));
        
        this.codeMap = codeMap;
        this.typesMap = codeMap.makeTypesMap();
    }

    public CompilerCodeMap getCodeMap() {
        return codeMap;
    }

    public TypesMap<TypeName> getTypesMap() {
        return typesMap;
    }
}
