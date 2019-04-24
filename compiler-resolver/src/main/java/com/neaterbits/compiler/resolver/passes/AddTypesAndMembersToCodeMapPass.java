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
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiPass;

import static com.neaterbits.compiler.resolver.util.ResolveUtil.forEachResolvedTypeNested;

public final class AddTypesAndMembersToCodeMapPass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
	extends MultiPass<
			ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>,
			CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> {

	private final CompilerCodeMap codeMap;
	private final ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel;
	
	public AddTypesAndMembersToCodeMapPass(ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel) {
		this(new IntCompilerCodeMap(), astModel);
	}

	public AddTypesAndMembersToCodeMapPass(CompilerCodeMap codeMap, ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel) {

		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(astModel);

		this.codeMap = codeMap;
		this.astModel = astModel;
	}

	@Override
	public AddTypesAndMembersToCodeMapResult<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> execute(
			ResolvedTypeDependencies<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> input) throws IOException {
		
		return makeCodeMap(input, codeMap, astModel);
	}
	
	public static <PARSED_FILE extends ParsedFile, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		AddTypesAndMembersToCodeMapResult<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> makeCodeMap(
				
			PostResolveFiles<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> postResolveFiles,
			CompilerCodeMap compilerCodeMap,
			ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel) {

		final ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFilesResult = postResolveFiles.getResolveFilesResult();
		
		final int numCompletelyResolvedFiles = resolveFilesResult.getResolvedFiles().size();
		final int numResolvedOrUnresolvedFiles = postResolveFiles.getFiles().size();
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder
			= new ArrayList<>(numCompletelyResolvedFiles);
		
		final Map<FileSpec, Integer> sourceFileNos = new HashMap<>();
		
		final ResolvedTypeCodeMapImpl<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap = makeCodeMap(
				resolveFilesResult.getResolvedFiles(),
				resolveFilesResult.getBuiltinTypes(),
				typesInDependencyOrder,
				compilerCodeMap,
				astModel,
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
	
	private static <COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> 
		ResolvedTypeCodeMapImpl<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> makeCodeMap(
			List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles,
			Collection<BUILTINTYPE> builtinTypes,
			List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder,
			CompilerCodeMap compilerCodeMap,
			ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel,
			Map<FileSpec, Integer> sourceFileNos) {
	
		final ResolvedTypeCodeMapImpl<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap = new ResolvedTypeCodeMapImpl<>(
				compilerCodeMap,
				builtinTypes,
				astModel);
		
		final Map<TypeName, ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedTypesByName = new HashMap<>();
		
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedFile : resolvedFiles) {
			
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				resolvedTypesByName.put(type.getTypeName(), type);
			});
		}
		
		// Verify that all dependencies are resolved
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedFile : resolvedFiles) {
	
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typeDependency : type.getExtendsFrom()) {
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
	
				final ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> type = resolvedTypesByName.get(completeName);
				
				boolean allExtendsFromAdded = true;
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typeDependency : type.getExtendsFrom()) {
						if (!codeMap.hasType(typeDependency.getCompleteName())) {
							allExtendsFromAdded = false;
							break;
						}
					}
				}
				
				if (allExtendsFromAdded) {
					codeMap.addType(type);
					
					iterator.remove();
	
					if (typesInDependencyOrder != null) {
						typesInDependencyOrder.add(type);
					}
				}
			}
		}
		
		final List<Integer> typeNosList = new ArrayList<>();
			
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedFile : resolvedFiles) {
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
		
		final MethodsResolver<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> methodsResolver = new MethodsResolver<>(codeMap, astModel);
	
		methodsResolver.resolveMethodsForAllTypes(resolvedFiles);
		
		return codeMap;
	}
}
