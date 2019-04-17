package com.neaterbits.compiler.resolver.passes;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.compiler.resolver.ASTTypesModel;
import com.neaterbits.compiler.resolver.FilesResolver;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolveLogger;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledFiles;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.model.CompilationUnitModel;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiPass;

public final class ResolveTypeDependenciesPass<COMPILATION_UNIT, PARSED_FILE extends ParsedFile, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		extends MultiPass<
			CompiledFiles<COMPLEXTYPE, COMPILATION_UNIT, PARSED_FILE>,
			ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> {

	private final CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel;
	private final Collection<BUILTINTYPE> builtinTypes;
	private final Function<ScopedName, LIBRARYTYPE> resolvedTypes;
	private final ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typesModel;

	public ResolveTypeDependenciesPass(
			CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel,
			Collection<BUILTINTYPE> builtinTypes,
			Function<ScopedName, LIBRARYTYPE> resolvedTypes,
			ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typesModel) {
		
		Objects.requireNonNull(compilationUnitModel);
		Objects.requireNonNull(builtinTypes);
		Objects.requireNonNull(resolvedTypes);
		Objects.requireNonNull(typesModel);
		
		this.compilationUnitModel = compilationUnitModel;
		this.builtinTypes = builtinTypes;
		this.resolvedTypes = resolvedTypes;
		this.typesModel = typesModel;
	}

	public static <COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> 
		ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveParsedFiles(
			
			Collection<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> allFiles,
			CompilationUnitModel<COMPILATION_UNIT> programModel,
			Collection<BUILTINTYPE> builtinTypes,
			Function<ScopedName, LIBRARYTYPE> resolvedTypes,
			ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typesModel) {

		final ResolveLogger<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE, COMPILATION_UNIT> logger = new ResolveLogger<>(System.out);

		final FilesResolver<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE, COMPILATION_UNIT> filesResolver
			= new FilesResolver<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE, COMPILATION_UNIT>(
					logger,
					builtinTypes,
					scopedName -> {
						
						System.out.println("## lookup scoped name " + scopedName);
						
						return resolvedTypes.apply(scopedName);
						
					},
					programModel,
					typesModel);
		
		final ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveResult = filesResolver.resolveFiles(allFiles);
		
		System.out.println("## resolved files: " + resolveResult.getResolvedFiles());
	
		return resolveResult;
	}

	@Override
	public ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		execute(CompiledFiles<COMPLEXTYPE, COMPILATION_UNIT, PARSED_FILE> input) throws IOException {

		final ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> result = resolveParsedFiles(
				input.getCompiledFiles(),
				compilationUnitModel,
				builtinTypes,
				resolvedTypes,
				typesModel);
		
		return new ResolvedTypeDependencies<>(input.getParsedFiles(), result);
	}
}
