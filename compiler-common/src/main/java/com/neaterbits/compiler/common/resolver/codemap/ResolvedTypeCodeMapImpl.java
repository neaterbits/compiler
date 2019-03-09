package com.neaterbits.compiler.common.resolver.codemap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.ResolvedTypeCodeMap;

import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateArray;

public final class ResolvedTypeCodeMapImpl implements ResolvedTypeCodeMap {

	private final ResolvedCodeMapImpl codeMap;
	private final NameToTypeNoMap nameToTypeNoMap;
	
	private final Map<ScopedName, Integer> scopedNameMap;
	
	private final MethodMapCache methodMapCache;
	
	private ResolvedType [] resolvedTypes;
	
	public ResolvedTypeCodeMapImpl(ResolvedCodeMapImpl codeMap, Collection<? extends BuiltinType> builtinTypes) {
		
		Objects.requireNonNull(codeMap);

		this.codeMap = codeMap;
		this.nameToTypeNoMap = new NameToTypeNoMap();
		this.scopedNameMap = new HashMap<>();
		this.methodMapCache = new MethodMapCache();
		
		// Add all builtin types
		for (BuiltinType builtinType : builtinTypes) {
			
			final ResolvedType resolvedBuiltinType = new ResolvedTypeBuiltin(builtinType);
			
			addType(resolvedBuiltinType);
		}
	}

	public int addFile(ResolvedFile file, int [] types) {
		return codeMap.addFile(types);
	}

	private int [] getExtendsFrom(ResolvedType type, TypeVariant typeVariant) {
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(typeVariant);
		
		final int [] extendsFrom;
		
		if (type.getExtendsFrom() != null) {
			
			int numExtendsFrom = 0;
			
			for (ResolvedTypeDependency typeDependency : type.getExtendsFrom()) {
				if (typeDependency.getTypeVariant().equals(typeVariant)) {
					++ numExtendsFrom;
				}
			}
			
			extendsFrom = new int[numExtendsFrom];
			
			int dstIdx = 0;
			
			for (ResolvedTypeDependency typeDependency : type.getExtendsFrom()) {
				if (typeDependency.getTypeVariant().equals(typeVariant)) {
					extendsFrom[dstIdx ++] = nameToTypeNoMap.getType(typeDependency.getCompleteName());
				}
			}
		}
		else {
			extendsFrom = null;
		}
		
		return extendsFrom;
	}
	
	public int addType(ResolvedType type) {

		final int typeNo = codeMap.addType(
				type.getTypeVariant(),
				type.getNumMethods(),
				getExtendsFrom(type, TypeVariant.CLASS),
				getExtendsFrom(type, TypeVariant.INTERFACE));

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

	private TypeInfo getTypeInfo(int typeNo) {
		return new TypeInfo(typeNo, codeMap.getTypeVariant(typeNo));
	}

	@Override
	public ComplexType<?, ?, ?> getType(int typeNo) {
		return resolvedTypes[typeNo].getType();
	}

	@Override
	public TypeInfo getClassExtendsFromTypeInfo(CompleteName classType) {
		final int type = codeMap.getClassThisExtendsFrom(getTypeNo(classType));

		return type < 0 ? null : getTypeInfo(type);
	}

	@Override
	public ResolvedType getClassThisExtendsFrom(CompleteName classType) {

		final int type = codeMap.getClassThisExtendsFrom(getTypeNo(classType));
		
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
	
	public int addMethod(int type, String name, CompleteName [] parameterTypes, MethodVariant methodVariant, int indexInType) {
		
		final int [] parameterTypeNos = new int[parameterTypes.length];
		
		for (int i = 0; i < parameterTypes.length; ++ i) {
			parameterTypeNos[i] = getTypeNo(parameterTypes[i]);
		}
		
		return codeMap.addMethod(type, name, parameterTypeNos, methodVariant, indexInType, methodMapCache);
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
	public TypeInfo getTypeInfo(BaseType type) {

		final NamedType namedType = (NamedType)type;
		
		final Integer typeNo = getTypeNo(namedType.getCompleteName());
		
		final TypeInfo typeInfo;
		
		if (typeNo != null) {
			typeInfo = getTypeInfo(typeNo);
		}
		else {
			typeInfo = null;
		}
		
		return typeInfo;
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

				final CompleteName typeName = parameterTypes[i].getCompleteName();
				final Integer parameterTypeNo = getTypeNo(typeName);
				
				if (parameterTypeNo == null) {
					throw new IllegalStateException("No typeNo for type " + typeName);
				}
				
				parameterTypeNos[i] = parameterTypeNo;
			}
		}
		
		return codeMap.getMethodInfo(typeNo, methodName.getName(), parameterTypeNos, methodMapCache);
	}
}
