package com.neaterbits.compiler.util.model;

import java.util.List;

import com.neaterbits.compiler.util.imports.TypeImport;

public interface CompilationUnitModel<COMPILATION_UNIT> {

	void iterate(COMPILATION_UNIT sourceFile, SourceTokenVisitor iterator, ResolvedTypes resolvedTypes, boolean visitPlaceholderElements);
	
	ISourceToken getTokenAt(COMPILATION_UNIT sourceFile, long offset, ResolvedTypes resolvedTypes);
	
	void iterateTypeImports(COMPILATION_UNIT sourceFile, TypeImportVisitor visitor);
	
	// Implicit type imports, eg. java.lang 
	List<TypeImport> getImplicitImports();

}
