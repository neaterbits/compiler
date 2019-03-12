package com.neaterbits.compiler.util.model;

import java.io.File;

public interface ProgramModel<MODULE, PARSED_FILE, COMPILATION_UNIT> extends CompilationUnitModel<COMPILATION_UNIT> {
	
	PARSED_FILE getParsedFile(MODULE module, File path);
	
	COMPILATION_UNIT getCompilationUnit(PARSED_FILE sourceFile);
	
}
