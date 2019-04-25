package com.neaterbits.compiler.resolver.passes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.resolver.ResolvedTypeCodeMapImpl;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ASTTypesModel;
import com.neaterbits.compiler.util.model.MethodVariant;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.ParsedFiles;

public final class MethodsResolver<PARSED_FILE extends ParsedFile, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	private final ParsedFiles<PARSED_FILE> parsedFiles;
	private final ResolvedTypeCodeMapImpl<COMPILATION_UNIT> codeMap;
	private final ASTTypesModel<COMPILATION_UNIT> astModel;
	
	public MethodsResolver(
			ParsedFiles<PARSED_FILE> parsedFiles,
			ResolvedTypeCodeMapImpl<COMPILATION_UNIT> codeMap,
			ASTTypesModel<COMPILATION_UNIT> astModel) {

		Objects.requireNonNull(parsedFiles);
		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(astModel);

		this.parsedFiles = parsedFiles;
		this.codeMap = codeMap;
		this.astModel = astModel;
	}


	void resolveMethodsForAllTypes(Collection<ResolvedFile> allFiles) {
	
		// Create a set of all types and just start resolving one by one
		
		final Map<TypeSpec, ResolvedType> map = new HashMap<>(allFiles.size());
	
		for (ResolvedFile file : allFiles) {
			getAllTypes(file.getTypes(), map);
		}
		
		resolveAllMethods(map);
	}
	
	
	private void getAllTypes(
			Collection<ResolvedType> types,
			Map<TypeSpec, ResolvedType> map) {
		
		for (ResolvedType type : types) {

			if (map.put(type.getSpec(), type) != null) {
				throw new IllegalStateException();
			}

			if (type.getNestedTypes() != null) {
				getAllTypes(types, map);
			}
		}
	}
	
	private void resolveAllMethods(Map<TypeSpec, ResolvedType> map) {
		
		final Set<TypeSpec> toResolve = new HashSet<>(map.keySet());

		while (!toResolve.isEmpty()) {

			final TypeSpec typeSpec = toResolve.iterator().next();
			
			resolveAllMethods(map, typeSpec);

			toResolve.remove(typeSpec);
		}
	}
	
	private void resolveAllMethods(Map<TypeSpec, ResolvedType> map, TypeSpec typeSpec) {
	
		Objects.requireNonNull(typeSpec);
		
		final ResolvedType resolvedType = map.get(typeSpec);
		
		addTypeAndMethods(resolvedType);
	}
	
	
	private void addTypeAndMethods(ResolvedType resolvedType) {
		
		Objects.requireNonNull(resolvedType);
		
		// Pass typeNo to references since faster lookup
		final COMPILATION_UNIT compilationUnit = parsedFiles.getParsedFile(resolvedType.getFile()).getCompilationUnit();
		
		final Integer typeNo = codeMap.getTypeNo(resolvedType.getTypeName());
		
		if (typeNo == null) {
			throw new IllegalArgumentException("No typeNo for " + resolvedType.getTypeName().toDebugString());
		}
		
		switch (resolvedType.getTypeVariant()) {
		case CLASS:

			addClassMembers(compilationUnit, resolvedType.getType(), typeNo);

			// Have added all methods, compute extends from/by
			codeMap.computeMethodExtends(resolvedType.getTypeName());
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	private void addClassMembers(COMPILATION_UNIT compilationUnit, UserDefinedTypeRef classType, int typeNo) {
		
		astModel.iterateClassMembers(
				
				compilationUnit,
				
				classType,
				
				(name, type, numArrayDimensions, isStatic, visibility, mutability, isVolatile, isTransient, indexInType) -> {
			
			codeMap.addField(typeNo, name, type, numArrayDimensions, isStatic, visibility, mutability, isVolatile, isTransient, indexInType);
			
		},
		
		(name, methodVariant, returnType, parameterTypes, indexInType) -> {
			
			addClassMethod(typeNo, name, methodVariant, parameterTypes, indexInType);
		});
	}
	
	private int addClassMethod(
			int typeNo,
			String name,
			MethodVariant methodVariant,
			TypeName [] parameterTypes,
			int indexInType) {
		
		final int methodNo = codeMap.addMethod(
				typeNo,
				name,
				parameterTypes,
				methodVariant,
				indexInType);

		return methodNo;
	}
}
