package com.neaterbits.compiler.resolver.passes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.resolver.ASTTypesModel;
import com.neaterbits.compiler.resolver.AddTypesAndMembersToCodeMapResult;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMapImpl;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiPass;
import com.neaterbits.compiler.util.passes.ParsedFiles;

import static com.neaterbits.compiler.resolver.util.ResolveUtil.forEachResolvedTypeNested;

public final class AddTypesAndMembersToCodeMapPass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
	extends MultiPass<
			ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT>,
			CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> {

	private final CompilerCodeMap codeMap;
	private final ASTTypesModel<COMPILATION_UNIT> astModel;
	
	public AddTypesAndMembersToCodeMapPass(ASTTypesModel<COMPILATION_UNIT> astModel) {
		this(new IntCompilerCodeMap(), astModel);
	}

	public AddTypesAndMembersToCodeMapPass(CompilerCodeMap codeMap, ASTTypesModel<COMPILATION_UNIT> astModel) {

		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(astModel);

		this.codeMap = codeMap;
		this.astModel = astModel;
	}

	@Override
	public AddTypesAndMembersToCodeMapResult<PARSED_FILE, COMPILATION_UNIT> execute(
			ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT> input) throws IOException {
		
		return makeCodeMap(input, codeMap, astModel);
	}
	
	public static <PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
		AddTypesAndMembersToCodeMapResult<PARSED_FILE, COMPILATION_UNIT> makeCodeMap(
				
			PostResolveFiles<PARSED_FILE, COMPILATION_UNIT> postResolveFiles,
			CompilerCodeMap compilerCodeMap,
			ASTTypesModel<COMPILATION_UNIT> astModel) {

		final ResolveFilesResult resolveFilesResult = postResolveFiles.getResolveFilesResult();
		
		final int numCompletelyResolvedFiles = resolveFilesResult.getResolvedFiles().size();
		final int numResolvedOrUnresolvedFiles = postResolveFiles.getFiles().size();
		
		final List<ResolvedType> typesInDependencyOrder
			= new ArrayList<>(numCompletelyResolvedFiles);
		
		final Map<FileSpec, Integer> sourceFileNos = new HashMap<>();
		
		final ResolvedTypeCodeMapImpl<COMPILATION_UNIT> codeMap = makeCodeMap(
				resolveFilesResult.getResolvedFiles(),
				resolveFilesResult.getBuiltinTypes(),
				typesInDependencyOrder,
				compilerCodeMap,
				astModel,
				postResolveFiles,
				sourceFileNos);

		if (numResolvedOrUnresolvedFiles > numCompletelyResolvedFiles) {
			
			// Add source file nos for the files
			// that were not completely resolved in previous passes
			
			final Set<FileSpec> resolvedFileSpecs = resolveFilesResult.getResolvedFiles().stream()
					.map(file -> file.getSpec())
					.collect(Collectors.toSet());
			
			final List<FileSpec> nonResolved = postResolveFiles.getFiles().stream()
					.map(file -> file.getParsedFile().getFileSpec())
					.filter(fileSpec -> !resolvedFileSpecs.contains(fileSpec))
					.collect(Collectors.toList());

			if (nonResolved.size() != numResolvedOrUnresolvedFiles - numCompletelyResolvedFiles) {
				throw new IllegalStateException();
			}
			
			for (FileSpec fileSpec : nonResolved) {
				final int sourceFileNo = codeMap.addNonResolvedFile(fileSpec);

				sourceFileNos.put(fileSpec, sourceFileNo);
			}
		}
		
		
		return new AddTypesAndMembersToCodeMapResult<>(
				postResolveFiles,
				sourceFileNos,
				resolveFilesResult.getResolvedFiles(),
				codeMap,
				typesInDependencyOrder);
	}
	
	private static <PARSED_FILE extends ParsedFile, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> 
		ResolvedTypeCodeMapImpl<COMPILATION_UNIT> makeCodeMap(
			List<ResolvedFile> resolvedFiles,
			Collection<BuiltinTypeRef> builtinTypes,
			List<ResolvedType> typesInDependencyOrder,
			CompilerCodeMap compilerCodeMap,
			ASTTypesModel<COMPILATION_UNIT> astModel,
			ParsedFiles<PARSED_FILE> parsedFiles,
			Map<FileSpec, Integer> sourceFileNos) {
	
		final ResolvedTypeCodeMapImpl<COMPILATION_UNIT> codeMap = new ResolvedTypeCodeMapImpl<>(
				compilerCodeMap,
				builtinTypes,
				astModel);
		
		final Map<TypeName, ResolvedType> resolvedTypesByName = new HashMap<>();
		
		for (ResolvedFile resolvedFile : resolvedFiles) {
			
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				resolvedTypesByName.put(type.getTypeName(), type);
			});
		}
		
		// Verify that all dependencies are resolved
		for (ResolvedFile resolvedFile : resolvedFiles) {
	
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency typeDependency : type.getExtendsFrom()) {
						if (!resolvedTypesByName.containsKey(typeDependency.getCompleteName())) {
							throw new IllegalStateException("Cannot find type " + typeDependency.getCompleteName());
						}
					}
				}
				
			});
		}
		
		final Set<TypeName> toAdd = new HashSet<>(resolvedTypesByName.keySet());
		
		
		// Since types extend from other types, we can only add those were we have added all base classes
		while (!toAdd.isEmpty()) {
			
			final Iterator<TypeName> iterator = toAdd.iterator();
			
			while (iterator.hasNext()) {
	
				final TypeName completeName = iterator.next();
				
				if (codeMap.hasType(completeName)) {
					throw new IllegalStateException();
				}
	
				final ResolvedType type = resolvedTypesByName.get(completeName);
				
				boolean allExtendsFromAdded = true;
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency typeDependency : type.getExtendsFrom()) {
						if (!codeMap.hasType(typeDependency.getCompleteName())) {
							allExtendsFromAdded = false;
							break;
						}
					}
				}
				
				if (allExtendsFromAdded) {
					
					final COMPILATION_UNIT compilationUnit = parsedFiles.getParsedFile(type.getFile()).getCompilationUnit();
					
					codeMap.addType(compilationUnit, type);
					
					iterator.remove();
	
					if (typesInDependencyOrder != null) {
						typesInDependencyOrder.add(type);
					}
				}
			}
		}
		
		final List<Integer> typeNosList = new ArrayList<>();
			
		for (ResolvedFile resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				typeNosList.add(codeMap.getTypeNo(type.getTypeName()));
			});
			
			final int [] typeNos = new int[typeNosList.size()];
			
			for (int i = 0; i < typeNosList.size(); ++ i) {
				typeNos[i] = typeNosList.get(i);
			}
			
			final int sourceFileNo = codeMap.addResolvedFile(resolvedFile, typeNos);
			sourceFileNos.put(resolvedFile.getSpec(), sourceFileNo);
			
			typeNosList.clear();
		}
		
		final MethodsResolver<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> methodsResolver
				= new MethodsResolver<>(parsedFiles, codeMap, astModel);
	
		methodsResolver.resolveMethodsForAllTypes(resolvedFiles);
		
		return codeMap;
	}
}
