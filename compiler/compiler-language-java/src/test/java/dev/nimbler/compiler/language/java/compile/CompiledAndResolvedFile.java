package dev.nimbler.compiler.language.java.compile;

import java.util.List;

import dev.nimbler.compiler.util.parse.CompileError;
import dev.nimbler.compiler.util.parse.ParsedFile;

public interface CompiledAndResolvedFile {

	<AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type);
	
	List<CompileError> getErrors();
	
	ParsedFile getParsedFile();
	
}
