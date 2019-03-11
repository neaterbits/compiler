package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;

public class UnresolvedReferenceReplacer {
	
	public static <BUILTINTYPE, COMPLEXTYPE> ReplaceTypeReferencesResult<BUILTINTYPE, COMPLEXTYPE> replaceUnresolvedTypeReferences(
			ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE> resolveFilesResult, ASTModel<BUILTINTYPE, COMPLEXTYPE> astModel) {
		
		final List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolvedFiles = resolveFilesResult.getResolvedFiles();
		
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE> resolvedFile : resolvedFiles) {
			replaceUnresolvedTypeReferences(resolvedFile.getTypes(), resolveFilesResult.getResolvedTypesMap(), resolveFilesResult.getBuiltinTypesMap());
		}
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> typesInDependencyOrder = new ArrayList<>(resolvedFiles.size());
		
		final ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> codeMap = CodeMapUtil.makeCodeMap(
				resolveFilesResult.getResolvedFiles(),
				resolveFilesResult.getBuiltinTypes(),
				typesInDependencyOrder,
				astModel);
		
		return new ReplaceTypeReferencesResult<>(resolveFilesResult.getResolvedFiles(), codeMap, typesInDependencyOrder);
	}
	
	static <BUILTINTYPE, COMPLEXTYPE> void replaceUnresolvedTypeReferences(
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> resolvedTypes,
			ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE> resolvedTypesMap,
			BuiltinTypesMap<BUILTINTYPE> builtinTypesMap) {
		
		for (ResolvedType<BUILTINTYPE, COMPLEXTYPE> resolvedType : resolvedTypes) {
			if (resolvedType.getNestedTypes() != null) {
				replaceUnresolvedTypeReferences(resolvedType.getNestedTypes(), resolvedTypesMap, builtinTypesMap);
			}

			if (resolvedType.getDependencies() != null) {
				
				for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE> typeDependency : resolvedType.getDependencies()) {
					
					final ResolvedType<BUILTINTYPE, COMPLEXTYPE> dependencyType = resolvedTypesMap.lookupType(typeDependency.getCompleteName());
					
					if (dependencyType != null) {
					
						final COMPLEXTYPE type = dependencyType.getType();
						
						typeDependency.replaceWithComplexType(type);
					}
					else {
						final BUILTINTYPE builtinType = builtinTypesMap.lookupType(typeDependency.getCompleteName().toScopedName());
						
						if (builtinType == null) {
							throw new IllegalStateException("Unknown type " + typeDependency.getCompleteName());
						}

						typeDependency.replaceWithBuiltinType(builtinType);
					}
				}
			}
		}
	}

}
