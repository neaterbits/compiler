package com.neaterbits.compiler.resolver.passes;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolvedTypesMap;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.util.BuiltinTypesMap;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.model.ASTTypesModel;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;
import com.neaterbits.compiler.util.model.LibraryTypeRef;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiPass;
import com.neaterbits.compiler.util.passes.ParsedFiles;

public class ReplaceResolvedTypeReferencesPass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
	extends MultiPass<
		ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT>,
		ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT>
> {

	private final LibraryTypes libraryTypes;
	private final ASTTypesModel<COMPILATION_UNIT> typesModel;
	
	public ReplaceResolvedTypeReferencesPass(LibraryTypes libraryTypes, ASTTypesModel<COMPILATION_UNIT> typesModel) {

		Objects.requireNonNull(libraryTypes);
		Objects.requireNonNull(typesModel);
		
		this.libraryTypes = libraryTypes;
		this.typesModel = typesModel;
	}

	@Override
	public ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT> execute(
			ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT> input) throws IOException {

		replaceResolvedTypeReferences(input.getResolveFilesResult(), libraryTypes, input, typesModel);
		
		return input;
	}
	
	public static <PARSED_FILE extends ParsedFile, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		void replaceResolvedTypeReferences(

			ResolveFilesResult resolveFilesResult,
			LibraryTypes libraryTypes,
			ParsedFiles<PARSED_FILE> parsedFiles,
			ASTTypesModel<COMPILATION_UNIT> astModel) {

		final List<ResolvedFile> resolvedFiles = resolveFilesResult
				.getResolvedFiles();

		for (ResolvedFile resolvedFile : resolvedFiles) {
			replaceResolvedTypeReferences(
					resolvedFile.getTypes(),
					resolveFilesResult.getResolvedTypesMap(),
					resolveFilesResult.getBuiltinTypesMap(),
					libraryTypes,
					parsedFiles.getParsedFile(resolvedFile.getSpec()).getCompilationUnit(),
					astModel);
		}
	}
	
	private static <COMPILATION_UNIT> void replaceResolvedTypeReferences(
			
			Collection<ResolvedType> resolvedTypes,
			ResolvedTypesMap resolvedTypesMap,
			BuiltinTypesMap builtinTypesMap,
			Function<ScopedName, LibraryTypeRef> libraryTypes,
			COMPILATION_UNIT compilationUnit,
			ASTTypesModel<COMPILATION_UNIT> astModel) {
		
		for (ResolvedType resolvedType : resolvedTypes) {
			if (resolvedType.getNestedTypes() != null) {
				replaceResolvedTypeReferences(resolvedType.getNestedTypes(), resolvedTypesMap, builtinTypesMap, libraryTypes, compilationUnit, astModel);
			}

			if (resolvedType.getDependencies() != null) {
				
				for (ResolvedTypeDependency typeDependency : resolvedType.getDependencies()) {
					
					final ResolvedType dependencyType = resolvedTypesMap.lookupType(typeDependency.getCompleteName());
					
					if (dependencyType != null) {
					
						final UserDefinedTypeRef type = dependencyType.getType();
						
						astModel.replaceWithUserDefinedType(compilationUnit, typeDependency.getTypeReferenceElement(), type);
					}
					else {
						final BuiltinTypeRef builtinType = builtinTypesMap.lookupType(typeDependency.getCompleteName().toScopedName());
						
						if (builtinType != null) {
							astModel.replaceWithBuiltinType(compilationUnit, typeDependency.getTypeReferenceElement(), builtinType);
						}
						else {

							final LibraryTypeRef libraryType = libraryTypes.apply(typeDependency.getScopedName());
							
							if (libraryType == null) {
								throw new IllegalStateException("Unknown type " + typeDependency.getCompleteName());
							}
							
							astModel.replaceWithLibraryType(compilationUnit, typeDependency.getTypeReferenceElement(), libraryType);
						}
					}
				}
			}
		}
	}

}
