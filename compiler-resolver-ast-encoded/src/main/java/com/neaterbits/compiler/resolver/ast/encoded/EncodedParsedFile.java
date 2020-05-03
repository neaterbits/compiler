package com.neaterbits.compiler.resolver.ast.encoded;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class EncodedParsedFile implements ParsedFile {

    private final FileSpec file;
    private final List<CompileError> errors;
    private final String log;
    private final EncodedCompilationUnit parsed;

    public EncodedParsedFile(
            FileSpec file,
            List<CompileError> errors,
            String log,
            EncodedCompilationUnit parsed) {
        
        this.file = file;
        this.errors = Collections.unmodifiableList(errors);
        this.log = log;
        this.parsed = parsed;
    }

    @Override
    public FileSpec getFileSpec() {
        return file;
    }

    @Override
    public List<CompileError> getErrors() {
        return errors;
    }

    public String getLog() {
        return log;
    }

    @Override
    public <AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <COMPILATION_UNIT> COMPILATION_UNIT getCompilationUnit() {
        return (COMPILATION_UNIT)parsed;
    }
}
