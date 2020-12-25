package com.neaterbits.compiler.java;

import java.util.List;

import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;

public interface CompiledAndResolvedFile {

	<AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type);
	
	List<CompileError> getErrors();
	
	ParsedFile getParsedFile();
	
}
