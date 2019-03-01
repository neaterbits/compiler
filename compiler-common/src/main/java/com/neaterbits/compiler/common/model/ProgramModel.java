package com.neaterbits.compiler.common.model;

import java.io.File;

public interface ProgramModel<MODULE, SOURCEFILE> {
	
	SOURCEFILE getSourceFile(File path);

	SourceToken getTokenAt(SOURCEFILE sourceFile, long offset);
	
}
