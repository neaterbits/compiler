package com.neaterbits.compiler.util.model;

import java.util.List;

import com.neaterbits.compiler.util.parse.CompileError;

public interface CompiledAndResolvedFile {

	<AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type);
	
	List<CompileError> getErrors();
	
}
