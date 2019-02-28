package com.neaterbits.compiler.common.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.common.BuiltinTypeReference;
import com.neaterbits.compiler.common.ComplexTypeReference;
import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

public class UnresolvedReferenceReplacer {
	
	public static ReplaceTypeReferencesResult replaceUnresolvedTypeReferences(ResolveFilesResult resolveFilesResult) {
		
		final List<ResolvedFile> resolvedFiles = resolveFilesResult.getResolvedFiles();
		
		for (ResolvedFile resolvedFile : resolvedFiles) {
			replaceUnresolvedTypeReferences(resolvedFile.getTypes(), resolveFilesResult.getResolvedTypesMap(), resolveFilesResult.getBuiltinTypesMap());
		}
		
		final List<ResolvedType> typesInDependencyOrder = new ArrayList<>(resolvedFiles.size());
		
		final ResolvedTypeCodeMapImpl codeMap = CodeMapUtil.makeCodeMap(resolveFilesResult.getResolvedFiles(), resolveFilesResult.getBuiltinTypes(), typesInDependencyOrder);
		
		return new ReplaceTypeReferencesResult(resolveFilesResult.getResolvedFiles(), codeMap, typesInDependencyOrder);
	}
	
	static final void replaceUnresolvedTypeReferences(Collection<ResolvedType> resolvedTypes, ResolvedTypesMap resolvedTypesMap, BuiltinTypesMap builtinTypesMap) {
		
		for (ResolvedType resolvedType : resolvedTypes) {
			if (resolvedType.getNestedTypes() != null) {
				replaceUnresolvedTypeReferences(resolvedType.getNestedTypes(), resolvedTypesMap, builtinTypesMap);
			}

			if (resolvedType.getDependencies() != null) {
				for (ResolvedTypeDependency typeDependency : resolvedType.getDependencies()) {
					final BaseASTElement element = typeDependency.getElement();

					if (!(element instanceof ResolveLaterTypeReference)) {
						throw new IllegalStateException();
					}
					
					final ResolvedType dependencyType = resolvedTypesMap.lookupType(typeDependency.getCompleteName());
					
					if (dependencyType != null) {
					
						final ComplexType<?> type = dependencyType.getType();
						
						element.replaceWith(new ComplexTypeReference(element.getContext(), type));
					}
					else {
						final BuiltinType builtinType = builtinTypesMap.lookupType(typeDependency.getCompleteName().toScopedName());
						
						if (builtinType == null) {
							throw new IllegalStateException("Unknown type " + typeDependency.getCompleteName());
						}
						
						element.replaceWith(new BuiltinTypeReference(element.getContext(), builtinType));
					}
				}
			}
		}
	}

}
