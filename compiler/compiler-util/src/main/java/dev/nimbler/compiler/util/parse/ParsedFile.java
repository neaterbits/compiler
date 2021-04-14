package dev.nimbler.compiler.util.parse;

import java.util.List;

import dev.nimbler.compiler.util.FileSpec;

public interface ParsedFile {

	FileSpec getFileSpec();

	List<CompileError> getErrors();
	
	<AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type);
	
	<COMPILATION_UNIT> COMPILATION_UNIT getCompilationUnit();
}
