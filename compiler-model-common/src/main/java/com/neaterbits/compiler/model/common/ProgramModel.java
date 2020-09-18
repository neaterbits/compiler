package com.neaterbits.compiler.model.common;

import com.neaterbits.compiler.util.FileSpec;

public interface ProgramModel<MODULE, PARSED_FILE, COMPILATION_UNIT> extends CompilationUnitModel<COMPILATION_UNIT> {
	
	PARSED_FILE getParsedFile(MODULE module, FileSpec path);
	
	COMPILATION_UNIT getCompilationUnit(PARSED_FILE sourceFile);
	
}
