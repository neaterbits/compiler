package com.neaterbits.compiler.resolver.ast.objects.model;

import java.util.List;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Import;
import com.neaterbits.compiler.util.imports.TypeImport;
import com.neaterbits.compiler.util.model.BaseImportsModel;
import com.neaterbits.compiler.util.model.ImportsModel;
import com.neaterbits.compiler.util.model.TypeImportVisitor;

public class ObjectImportsModel
    extends BaseImportsModel<CompilationUnit>
    implements ImportsModel<CompilationUnit> {

	public ObjectImportsModel(List<TypeImport> implicitImports) {
        super(implicitImports);
    }

    @Override
	public final void iterateTypeImports(CompilationUnit sourceFile, TypeImportVisitor visitor) {
		for (Import typeImport : sourceFile.getImports()) {
			typeImport.visit(visitor);
		}
	}
}
