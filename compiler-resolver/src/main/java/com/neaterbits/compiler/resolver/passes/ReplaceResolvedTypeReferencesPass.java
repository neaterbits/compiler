package com.neaterbits.compiler.resolver.passes;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.ASTTypesModel;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolvedTypesMap;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.util.BuiltinTypesMap;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiPass;

public class ReplaceResolvedTypeReferencesPass<PARSED_FILE extends ParsedFile, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
	extends MultiPass<
		ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>,
		ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
> {

	private final ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typesModel;
	
	public ReplaceResolvedTypeReferencesPass(ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typesModel) {

		Objects.requireNonNull(typesModel);
		
		this.typesModel = typesModel;
	}

	@Override
	public ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> execute(
			ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> input) throws IOException {

		replaceResolvedTypeReferences(input.getResolveFilesResult(), typesModel);
		
		return input;
	}
	
	public static <BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		void replaceResolvedTypeReferences(

			ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFilesResult,
			ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel) {

		final List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles = resolveFilesResult
				.getResolvedFiles();

		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedFile : resolvedFiles) {
			replaceResolvedTypeReferences(resolvedFile.getTypes(), resolveFilesResult.getResolvedTypesMap(),
					resolveFilesResult.getBuiltinTypesMap());
		}
	}
	
	private static <BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> void replaceResolvedTypeReferences(
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedTypes,
			ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedTypesMap,
			BuiltinTypesMap<BUILTINTYPE> builtinTypesMap) {
		
		for (ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedType : resolvedTypes) {
			if (resolvedType.getNestedTypes() != null) {
				replaceResolvedTypeReferences(resolvedType.getNestedTypes(), resolvedTypesMap, builtinTypesMap);
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
