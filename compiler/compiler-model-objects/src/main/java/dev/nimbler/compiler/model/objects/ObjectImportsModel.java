package dev.nimbler.compiler.model.objects;

import java.util.List;

import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.Import;
import dev.nimbler.compiler.model.common.BaseImportsModel;
import dev.nimbler.compiler.model.common.ImportsModel;
import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.types.imports.TypeImportVisitor;

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
