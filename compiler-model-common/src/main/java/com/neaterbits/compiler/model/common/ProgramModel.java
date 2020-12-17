package com.neaterbits.compiler.model.common;

import java.util.Collection;

import com.neaterbits.compiler.util.FileSpec;

public interface ProgramModel<PARSED_FILE, COMPILATION_UNIT>
        extends CompilationUnitModel<COMPILATION_UNIT> {
	
	PARSED_FILE getParsedFile(Collection<PARSED_FILE> module, FileSpec path);
	
	COMPILATION_UNIT getCompilationUnit(PARSED_FILE sourceFile);
	
}
