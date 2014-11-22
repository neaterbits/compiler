package com.neaterbits.compiler.common.resolver.codemap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.resolver.CodeMap;

import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateArray;

public final class ResolvedTypeCodeMapImpl implements CodeMap {

	private final CodeMapImpl codeMap;
	private final NameToTypeNoMap nameToTypeNoMap;
	
	private final Map<ScopedName, Integer> scopedNameMap;
	
	private final MethodMapCache methodMapCache;
	
	private ResolvedType [] resolvedTypes;
	
	public ResolvedTypeCodeMapImpl(CodeMapImpl codeMap) {
		
		Objects.requireNonNull(codeMap);

		this.codeMap = codeMap;
		this.nameToTypeNoMap = new NameToTypeNoMap();
		this.scopedNameMap = new HashMap<>();
		this.methodMapCache = new MethodMapCache();
	}

	public int addFile(ResolvedFile file, int [] types) {
		return codeMap.addFile(types);
	}

	public int addType(ResolvedType type) {

		final int [] extendsFrom;
		
		if (type.getExtendsFrom() != null) {
			extendsFrom = new int[type.getExtendsFrom().size()];
			
			int dstIdx = 0;
			
			for (ResolvedTypeDependency typeDependency : type.getExtendsFrom()) {
				extendsFrom[dstIdx ++] = nameToTypeNoMap.getType(typeDependency.getCompleteName());
			}
		}
		else {
			extendsFrom = null;
		}
		
		final int typeNo = codeMap.addType(type.getTypeVariant(), type.getNumMethods(), extendsFrom);

		this.resolvedTypes = allocateArray(
				this.resolvedTypes,
				typeNo + 1,
				length -> new ResolvedType[length]);
		
		resolvedTypes[typeNo] = type;
		
		nameToTypeNoMap.addMapping(type.getCompleteName(), typeNo);
		scopedNameMap.put(type.getScopedName(), typeNo);
		
		return typeNo;
	}
	
	public ResolvedType getType(CompleteName completeName) {
		final int typeNo = getTypeNo(completeName);
		
		return resolvedTypes[typeNo];
	}
	
	public ResolvedType getType(ScopedName scopedName) {
		
		final Integer typeNo = scopedNameMap.get(scopedName);
		
		return typeNo != null ? resolvedTypes[typeNo] : null;
	}
	
	@Override
	public ResolvedType getClassExtendsFrom(CompleteName classType) {

		final int type = codeMap.getClassExtendsFrom(getTypeNo(classType));
		
		return type < 0 ? null : resolvedTypes[type];
	}

	@Override
	public Collection<ResolvedType> getInterfacesImplement(CompleteName classType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ResolvedType> getInterfacesExtendFrom(CompleteName interfaceType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean hasType(CompleteName completeName) {
		return nameToTypeNoMap.getType(completeName) != null;
	}

	public Integer getTypeNo(CompleteName type) {
		
		Objects.requireNonNull(type);
		
		return nameToTypeNoMap.getType(type);
	}
	
	public int addMethod(int type, String name, CompleteName [] parameterTypes, MethodVariant methodVariant) {
		
		final int [] parameterTypeNos = new int[parameterTypes.length];
		
		for (int i = 0; i < parameterTypes.length; ++ i) {
			parameterTypeNos[i] = getTypeNo(parameterTypes[i]);
		}
		
		return codeMap.addMethod(type, name, parameterTypeNos, methodVariant, methodMapCache);
	}

	public void computeMethodExtends(CompleteName completeName) {
		codeMap.computeMethodExtends(getTypeNo(completeName));
	}

	@Override
	public List<ResolvedType> getDirectSubtypes(CompleteName type) {
		
		Objects.requireNonNull(type);
		
		final int typeNo = getTypeNo(type);
		
		final int [] types = codeMap.getDirectSubtypes(typeNo);
		
		final List<ResolvedType> result = new ArrayList<>(types.length);
		
		for (int resultTypeNo : types) {
			result.add(resolvedTypes[resultTypeNo]);
		}

		return result;
	}

	@Override
	public List<ResolvedType> getAllSubtypes(CompleteName type) {
		
		Objects.requireNonNull(type);
		
		final int typeNo = getTypeNo(type);
		
		final int [] types = codeMap.getAllSubtypes(typeNo);
		
		final List<ResolvedType> result = new ArrayList<>(types.length);
		
		for (int resultTypeNo : types) {
			result.add(resolvedTypes[resultTypeNo]);
		}

		return result;
	}

	@Override
	public MethodInfo getMethodInfo(ClassType type, MethodName methodName, NamedType [] parameterTypes) {
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(methodName);
		
		final int typeNo = getTypeNo(type.getCompleteName());
		
		final int [] parameterTypeNos;
		
		if (parameterTypes == null) {
			parameterTypeNos = null;
		}
		else {
			parameterTypeNos = new int[parameterTypes.length];
		
			for (int i = 0; i < parameterTypes.length; ++ i) {
				parameterTypeNos[i] = getTypeNo(parameterTypes[i].getCompleteName());
			}
		}
		
		return codeMap.getMethodInfo(typeNo, methodName.getName(), parameterTypeNos, methodMapCache);
	}
}
