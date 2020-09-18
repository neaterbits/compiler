package com.neaterbits.compiler.model.objects;

import java.util.List;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Import;
import com.neaterbits.compiler.model.common.BaseImportsModel;
import com.neaterbits.compiler.model.common.ImportsModel;
import com.neaterbits.compiler.types.imports.TypeImport;
import com.neaterbits.compiler.types.imports.TypeImportVisitor;

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
