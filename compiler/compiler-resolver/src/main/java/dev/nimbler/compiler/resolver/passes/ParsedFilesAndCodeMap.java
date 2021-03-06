package dev.nimbler.compiler.resolver.passes;

import java.util.Collection;
import java.util.Objects;

import dev.nimbler.compiler.model.common.passes.ParsedFiles;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;

public class ParsedFilesAndCodeMap<PARSED_FILE extends ParsedFile>
    extends ParsedFiles<PARSED_FILE> {

    private final CompilerCodeMap codeMap;

    public ParsedFilesAndCodeMap(Collection<PARSED_FILE> parsedFiles, CompilerCodeMap codeMap) {
        super(parsedFiles);

        Objects.requireNonNull(codeMap);

        this.codeMap = codeMap;
    }

    public ParsedFilesAndCodeMap(ParsedFilesAndCodeMap<PARSED_FILE> other) {
        super(other);

        this.codeMap = other.codeMap;
    }

    public final CompilerCodeMap getCodeMap() {
        return codeMap;
    }
}
