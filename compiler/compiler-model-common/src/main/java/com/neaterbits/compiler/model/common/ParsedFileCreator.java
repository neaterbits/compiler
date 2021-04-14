package com.neaterbits.compiler.model.common;

import java.util.List;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.CompileError;

@FunctionalInterface
public interface ParsedFileCreator<PARSED_FILE, COMPILATION_UNIT> {

    PARSED_FILE createParsedFile(
            FileSpec file,
            COMPILATION_UNIT parsed,
            List<CompileError> errors,
            String log);
}
