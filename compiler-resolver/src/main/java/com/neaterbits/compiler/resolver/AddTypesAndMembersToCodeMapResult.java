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
import com.neaterbits.compiler.types.FieldInfo;
import com.neaterbits.compiler.types.MethodInfo;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class AddTypesAndMembersToCodeMapResult<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
		extends MappedFiles<PARSED_FILE, COMPILATION_UNIT>
		implements CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> {

	private final Collection<ResolvedFile> resolvedFiles;
	private final ResolvedTypeCodeMapImpl<COMPILATION_UNIT> codeMap;
	private final Collection<ResolvedType> typesInDependencyOrder;

	public AddTypesAndMembersToCodeMapResult(
			PostResolveFiles<PARSED_FILE, COMPILATION_UNIT> other,
			Map<FileSpec, Integer> sourceFileNos,
			Collection<ResolvedFile> resolvedFiles,
			ResolvedTypeCodeMapImpl<COMPILATION_UNIT> codeMap,
			Collection<ResolvedType> typesInDependencyOrder) {

		super(other, sourceFileNos);
		
		Objects.requireNonNull(resolvedFiles);
		Objects.requireNonNull(codeMap);
		
		this.resolvedFiles = resolvedFiles;
		this.codeMap = codeMap;
		this.typesInDependencyOrder = typesInDependencyOrder;
	}
	
	public Collection<ResolvedFile> getResolvedFiles() {
		return resolvedFiles;
	}

	public ResolvedTypeCodeMap getCodeMap() {
		return codeMap;
	}

	@Override
	public final CompilerCodeMapGetters getCompilerCodeMap() {
		return codeMap.getCompilerCodeMap();
	}

	public Collection<ResolvedType> getTypesInDependencyOrder() {
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
