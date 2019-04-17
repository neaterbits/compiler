package com.neaterbits.compiler.util.passes;

import java.util.List;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.CompileError;

@FunctionalInterface
public interface CreateParsedFile<COMPILATION_UNIT, PARSED_FILE> {
	
	PARSED_FILE create(FileSpec file, COMPILATION_UNIT parsed, List<CompileError> errors, String log);
	
}
