package com.neaterbits.compiler.model.common;

import java.util.List;

import com.neaterbits.compiler.types.imports.TypeImport;
import com.neaterbits.compiler.types.imports.TypeImportVisitor;

public interface ImportsModel<COMPILATION_UNIT> {

	void iterateTypeImports(COMPILATION_UNIT sourceFile, TypeImportVisitor visitor);

	// Implicit type imports, eg. java.lang 
	List<TypeImport> getImplicitImports();

}
