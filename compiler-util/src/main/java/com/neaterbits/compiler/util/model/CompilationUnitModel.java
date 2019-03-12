package com.neaterbits.compiler.util.model;

public interface CompilationUnitModel<COMPILATION_UNIT> {

	void iterate(COMPILATION_UNIT sourceFile, SourceTokenVisitor iterator, ResolvedTypes resolvedTypes);
	
	ISourceToken getTokenAt(COMPILATION_UNIT sourceFile, long offset, ResolvedTypes resolvedTypes);
	
	void iterateTypeImports(COMPILATION_UNIT sourceFile, TypeImportVisitor visitor);

}
