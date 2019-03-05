package com.neaterbits.compiler.common.model;

import java.io.File;

public interface ProgramModel<MODULE, PARSED_FILE, COMPILATION_UNIT> {
	
	PARSED_FILE getParsedFile(MODULE module, File path);
	
	COMPILATION_UNIT getCompilationUnit(PARSED_FILE sourceFile);

	ISourceToken getTokenAt(COMPILATION_UNIT sourceFile, long offset);
	
}
