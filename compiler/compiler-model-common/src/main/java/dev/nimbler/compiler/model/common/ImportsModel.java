package dev.nimbler.compiler.model.common;

import java.util.List;

import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.types.imports.TypeImportVisitor;

public interface ImportsModel<COMPILATION_UNIT> {

	void iterateTypeImports(COMPILATION_UNIT sourceFile, TypeImportVisitor visitor);

	// Implicit type imports, eg. java.lang 
	List<TypeImport> getImplicitImports();

}
