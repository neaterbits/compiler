package dev.nimbler.compiler.model.common;

import java.util.List;

import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.parse.CompileError;

@FunctionalInterface
public interface ParsedFileCreator<PARSED_FILE, COMPILATION_UNIT> {

    PARSED_FILE createParsedFile(
            FileSpec file,
            COMPILATION_UNIT parsed,
            List<CompileError> errors,
            String log);
}
