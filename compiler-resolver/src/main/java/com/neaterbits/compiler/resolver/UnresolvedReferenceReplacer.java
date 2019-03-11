package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;

public class UnresolvedReferenceReplacer {
	
	public static <BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
			ReplaceTypeReferencesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> replaceUnresolvedTypeReferences(
						
			ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFilesResult, ASTModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel) {
		
		final List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles = resolveFilesResult.getResolvedFiles();
		
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedFile : resolvedFiles) {
			replaceUnresolvedTypeReferences(resolvedFile.getTypes(), resolveFilesResult.getResolvedTypesMap(), resolveFilesResult.getBuiltinTypesMap());
		}
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder = new ArrayList<>(resolvedFiles.size());
		
		final ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap = CodeMapUtil.makeCodeMap(
				resolveFilesResult.getResolvedFiles(),
				resolveFilesResult.getBuiltinTypes(),
				typesInDependencyOrder,
				astModel);
		
		return new ReplaceTypeReferencesResult<>(resolveFilesResult.getResolvedFiles(), codeMap, typesInDependencyOrder);
	}
	
	static <BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> void replaceUnresolvedTypeReferences(
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedTypes,
			ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedTypesMap,
			BuiltinTypesMap<BUILTINTYPE> builtinTypesMap) {
		
		for (ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedType : resolvedTypes) {
			if (resolvedType.getNestedTypes() != null) {
				replaceUnresolvedTypeReferences(resolvedType.getNestedTypes(), resolvedTypesMap, builtinTypesMap);
			}

			if (resolvedType.getDependencies() != null) {
				
				for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typeDependency : resolvedType.getDependencies()) {
					
					final ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> dependencyType = resolvedTypesMap.lookupType(typeDependency.getCompleteName());
					
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
