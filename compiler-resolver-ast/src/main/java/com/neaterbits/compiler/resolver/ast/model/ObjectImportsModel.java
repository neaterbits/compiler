package com.neaterbits.compiler.resolver.ast.model;

import java.util.List;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Import;
import com.neaterbits.compiler.util.imports.TypeImport;
import com.neaterbits.compiler.util.model.ImportsModel;
import com.neaterbits.compiler.util.model.TypeImportVisitor;

public class ObjectImportsModel implements ImportsModel<CompilationUnit> {

	private final List<TypeImport> implicitImports;
	
	public ObjectImportsModel(List<TypeImport> implicitImports) {
		this.implicitImports = implicitImports;
	}

	@Override
	public final void iterateTypeImports(CompilationUnit sourceFile, TypeImportVisitor visitor) {
		for (Import typeImport : sourceFile.getImports()) {
			typeImport.visit(visitor);
		}
	}

	@Override
	public final List<TypeImport> getImplicitImports() {
		return implicitImports;
	}
}
