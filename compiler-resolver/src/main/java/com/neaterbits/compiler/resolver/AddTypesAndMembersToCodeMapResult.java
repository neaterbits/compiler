package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMapGetters;
import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.resolver.passes.MappedFiles;
import com.neaterbits.compiler.resolver.passes.PostResolveFiles;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.FieldInfo;
import com.neaterbits.compiler.util.model.MethodInfo;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class AddTypesAndMembersToCodeMapResult<PARSED_FILE extends ParsedFile, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		extends MappedFiles<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		implements CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> {

	private final Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles;
	private final ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap;
	private final Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder;

	public AddTypesAndMembersToCodeMapResult(
			PostResolveFiles<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> other,
			Map<FileSpec, Integer> sourceFileNos,
			Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles,
			ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap,
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder) {

		super(other, sourceFileNos);
		
		Objects.requireNonNull(resolvedFiles);
		Objects.requireNonNull(codeMap);
		
		this.resolvedFiles = resolvedFiles;
		this.codeMap = codeMap;
		this.typesInDependencyOrder = typesInDependencyOrder;
	}
	
	public Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getResolvedFiles() {
		return resolvedFiles;
	}

	public ResolvedTypeCodeMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> getCodeMap() {
		return codeMap;
	}

	@Override
	public final CompilerCodeMapGetters getCompilerCodeMap() {
		return codeMap.getCompilerCodeMap();
	}

	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getTypesInDependencyOrder() {
		return typesInDependencyOrder;
	}

	@Override
	public Integer getTypeNo(TypeName type) {
		return codeMap.getTypeNo(type);
	}

	@Override
	public FieldInfo getFieldInfo(TypeName type, String fieldName) {
		return codeMap.getFieldInfo(type, fieldName);
	}

	@Override
	public MethodInfo getMethodInfo(TypeName type, String methodName, TypeName[] parameterTypes) {
		return codeMap.getMethodInfo(type, methodName, parameterTypes);
	}

	@Override
	public int addToken(int sourceFile, int parseTreeRef) {
		return codeMap.addToken(sourceFile, parseTreeRef);
	}

	@Override
	public CrossReferenceUpdater getCrossReferenceUpdater() {
		return codeMap.getCrossReferenceUpdater();
	}
}
