package dev.nimbler.compiler.language.java.compile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.util.parse.CompileError;
import dev.nimbler.compiler.util.parse.ParsedFile;

public class CompiledAndResolvedFileImpl<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
        implements CompiledAndResolvedFile {
    
    private final ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed;

    public CompiledAndResolvedFileImpl(ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed) {
        
        Objects.requireNonNull(parsed);
        
        this.parsed = parsed;
    }

    @Override
    public <AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type) {
        
        return parsed.getParsedFile().getASTElements(type);
    }

    @Override
    public List<CompileError> getErrors() {
        
        final List<CompileError> parseErrors = parsed.getParsedFile().getErrors();
        
        final List<CompileError> list = new ArrayList<>(parseErrors.size() + parsed.getResolveErrorsList().size());
        
        list.addAll(parseErrors);
        list.addAll(parsed.getResolveErrorsList());
        
        return list;
    }

    @Override
    public ParsedFile getParsedFile() {
        return parsed.getParsedFile();
    }
}