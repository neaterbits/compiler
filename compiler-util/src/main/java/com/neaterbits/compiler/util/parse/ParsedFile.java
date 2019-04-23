package com.neaterbits.compiler.util.parse;

import java.util.List;

import com.neaterbits.compiler.util.FileSpec;

public interface ParsedFile {

	FileSpec getFileSpec();

	List<CompileError> getErrors();
	
	<AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type);
	
	<COMPILATION_UNIT> COMPILATION_UNIT getCompilationUnit();
}
