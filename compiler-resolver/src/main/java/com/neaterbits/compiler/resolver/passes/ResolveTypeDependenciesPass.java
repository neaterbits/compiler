package com.neaterbits.compiler.resolver.passes;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.resolver.ASTTypesModel;
import com.neaterbits.compiler.resolver.FilesResolver;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolveLogger;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledFiles;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;
import com.neaterbits.compiler.util.model.ImportsModel;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiPass;

public final class ResolveTypeDependenciesPass<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
		extends MultiPass<
			CompiledFiles<COMPILATION_UNIT, PARSED_FILE>,
			ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT>> {

	private final ImportsModel<COMPILATION_UNIT> importsModel;
	private final Collection<BuiltinTypeRef> builtinTypes;
	private final LibraryTypes libraryTypes;
	private final ASTTypesModel<COMPILATION_UNIT> typesModel;

	public ResolveTypeDependenciesPass(
			ImportsModel<COMPILATION_UNIT> importsModel,
			Collection<BuiltinTypeRef> builtinTypes,
			LibraryTypes libraryTypes,
			ASTTypesModel<COMPILATION_UNIT> typesModel) {
		
		Objects.requireNonNull(importsModel);
		Objects.requireNonNull(builtinTypes);
		Objects.requireNonNull(libraryTypes);
		Objects.requireNonNull(typesModel);
		
		this.importsModel = importsModel;
		this.builtinTypes = builtinTypes;
		this.libraryTypes = libraryTypes;
		this.typesModel = typesModel;
	}

	public static <COMPILATION_UNIT> 
		ResolveFilesResult resolveParsedFiles(
			
			Collection<CompiledFile<COMPILATION_UNIT>> allFiles,
			ImportsModel<COMPILATION_UNIT> importsModel,
			Collection<BuiltinTypeRef> builtinTypes,
			LibraryTypes resolvedTypes,
			ASTTypesModel<COMPILATION_UNIT> typesModel) {

		final ResolveLogger<COMPILATION_UNIT> logger = new ResolveLogger<>(System.out);

		final FilesResolver<COMPILATION_UNIT> filesResolver
			= new FilesResolver<COMPILATION_UNIT>(
					logger,
					builtinTypes,
					scopedName -> {
						
						System.out.println("## lookup scoped name " + scopedName);
						
						return resolvedTypes.apply(scopedName);
						
					},
					importsModel,
					typesModel);
		
		final ResolveFilesResult resolveResult = filesResolver.resolveFiles(allFiles);
		
		System.out.println("## resolved files: " + resolveResult.getResolvedFiles());
	
		return resolveResult;
	}

	@Override
	public ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT>
		execute(CompiledFiles<COMPILATION_UNIT, PARSED_FILE> input) throws IOException {

		final ResolveFilesResult result = resolveParsedFiles(
				input.getCompiledFiles(),
				importsModel,
				builtinTypes,
				libraryTypes,
				typesModel);
		
		return new ResolvedTypeDependencies<>(input.getParsedFiles(), result);
	}
}
