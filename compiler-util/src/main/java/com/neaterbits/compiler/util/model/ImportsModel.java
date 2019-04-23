package com.neaterbits.compiler.util.model;

import java.util.List;

import com.neaterbits.compiler.util.imports.TypeImport;

public interface ImportsModel<COMPILATION_UNIT> {

	void iterateTypeImports(COMPILATION_UNIT sourceFile, TypeImportVisitor visitor);

	// Implicit type imports, eg. java.lang 
	List<TypeImport> getImplicitImports();

}
