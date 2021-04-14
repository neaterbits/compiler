package dev.nimbler.compiler.model.common.util;

import java.util.ArrayList;
import java.util.List;

import dev.nimbler.compiler.model.common.ImportsModel;
import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.types.imports.TypeImportVisitor;
import dev.nimbler.language.common.types.ScopedName;

class FileImports {

	static <COMPILATION_UNIT>
		List<ScopedName> getAllNameCombinations(
				COMPILATION_UNIT compilationUnit,
				ScopedName scopedName,
				ImportsModel<COMPILATION_UNIT> importsModel) {
		
		final List<ScopedName> result;
		
		if (scopedName.getScope() != null && !scopedName.getScope().isEmpty()) {
			throw new IllegalArgumentException("Already scoped");
		}
		else {

			result = new ArrayList<>();
			
			final TypeImportVisitor visitor = new TypeImportVisitor() {
				
				@Override
				public void onStaticMethodWildcard(String[] namespace, String typeName) {
					if (typeName.equals(scopedName.getName())) {
						result.add(ScopedName.makeScopedName(namespace, typeName));
					}
				}
				
				@Override
				public void onNamespaceOrTypeName(String[] namespaceOrTypeName) {
					
					result.add(ScopedName.makeScopedName(namespaceOrTypeName, namespaceOrTypeName.length - 1, namespaceOrTypeName[namespaceOrTypeName.length - 1]));
					result.add(ScopedName.makeScopedName(namespaceOrTypeName, namespaceOrTypeName.length, scopedName.getName()));
				}
				
				@Override
				public void onKnownStaticMethod(String[] namespace, String typeName, String methodName) {
					
				}
				
				@Override
				public void onKnownNamespace(String[] namespace, String typeName) {
					
					if (typeName.equals(scopedName.getName())) {
						result.add(ScopedName.makeScopedName(namespace, typeName));
					}
				}
			};

			importsModel.iterateTypeImports(compilationUnit, visitor);
			
			final List<TypeImport> implicitImports = importsModel.getImplicitImports();
			
			if (implicitImports != null) {
				for (TypeImport implicitImport : implicitImports) {
					implicitImport.visit(visitor);
				}
			}
		}

		return result;
	}
}
