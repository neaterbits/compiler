package com.neaterbits.compiler.common.resolver.codemap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
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
				extendsFrom[dstIdx ++] = nameToTypeNoMap.getType(typeDependency.getFullTypeName());
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
		
		nameToTypeNoMap.addMapping(type.getFullTypeName(), typeNo);
		scopedNameMap.put(type.getScopedName(), typeNo);
		
		return typeNo;
	}
	
	public ResolvedType getType(FullTypeName fullTypeName) {
		final int typeNo = getTypeNo(fullTypeName);
		
		return resolvedTypes[typeNo];
	}
	
	public ResolvedType getType(ScopedName scopedName) {
		
		final Integer typeNo = scopedNameMap.get(scopedName);
		
		return typeNo != null ? resolvedTypes[typeNo] : null;
	}
	
	@Override
	public ResolvedType getClassExtendsFrom(FullTypeName classType) {

		final int type = codeMap.getClassExtendsFrom(getTypeNo(classType));
		
		return type < 0 ? null : resolvedTypes[type];
	}

	@Override
	public Collection<ResolvedType> getInterfacesImplement(FullTypeName classType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ResolvedType> getInterfacesExtendFrom(FullTypeName interfaceType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean hasType(FullTypeName fullTypeName) {
		return nameToTypeNoMap.getType(fullTypeName) != null;
	}

	public Integer getTypeNo(FullTypeName type) {
		
		Objects.requireNonNull(type);
		
		return nameToTypeNoMap.getType(type);
	}
	
	public int addMethod(int type, String name, FullTypeName [] parameterTypes, MethodVariant methodVariant) {
		
		final int [] parameterTypeNos = new int[parameterTypes.length];
		
		for (int i = 0; i < parameterTypes.length; ++ i) {
			parameterTypeNos[i] = getTypeNo(parameterTypes[i]);
		}
		
		return codeMap.addMethod(type, name, parameterTypeNos, methodVariant, methodMapCache);
	}

	public void computeMethodExtends(FullTypeName fullTypeName) {
		codeMap.computeMethodExtends(getTypeNo(fullTypeName));
	}

	@Override
	public List<ResolvedType> getDirectSubtypes(FullTypeName type) {
		
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
	public List<ResolvedType> getAllSubtypes(FullTypeName type) {
		
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
		
		final int typeNo = getTypeNo(type.getFullTypeName());
		
		final int [] parameterTypeNos;
		
		if (parameterTypes == null) {
			parameterTypeNos = null;
		}
		else {
			parameterTypeNos = new int[parameterTypes.length];
		
			for (int i = 0; i < parameterTypes.length; ++ i) {
				parameterTypeNos[i] = getTypeNo(parameterTypes[i].getFullTypeName());
			}
		}
		
		return codeMap.getMethodInfo(typeNo, methodName.getName(), parameterTypeNos, methodMapCache);
	}
}
