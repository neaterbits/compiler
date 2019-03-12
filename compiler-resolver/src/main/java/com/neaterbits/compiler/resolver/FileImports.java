package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.model.CompilationUnitModel;
import com.neaterbits.compiler.util.model.TypeImportVisitor;

class FileImports {

	static <COMPILATION_UNIT>
		List<ScopedName> getAllNameCombinations(
				COMPILATION_UNIT compilationUnit,
				ScopedName scopedName,
				CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel) {
		
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

			compilationUnitModel.iterateTypeImports(compilationUnit, visitor);
		}

		return result;
	}
}
