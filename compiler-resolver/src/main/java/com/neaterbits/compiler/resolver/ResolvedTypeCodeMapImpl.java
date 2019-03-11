package com.neaterbits.compiler.resolver;

import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.resolver.codemap.ResolvedTypeBuiltin;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.codemap.MethodInfo;
import com.neaterbits.compiler.codemap.MethodMapCache;
import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.codemap.NameToTypeNoMap;
import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.codemap.TypeVariant;

public final class ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> implements ResolvedTypeCodeMap<BUILTINTYPE, COMPLEXTYPE> {

	private final ResolvedCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> codeMap;
	private final ASTModel<BUILTINTYPE, COMPLEXTYPE> astModel;
	private final NameToTypeNoMap nameToTypeNoMap;
	
	private final Map<ScopedName, Integer> scopedNameMap;
	
	private final MethodMapCache methodMapCache;
	
	private ResolvedType<BUILTINTYPE, COMPLEXTYPE> [] resolvedTypes;
	
	public ResolvedTypeCodeMapImpl(
			ResolvedCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> codeMap,
			Collection<BUILTINTYPE> builtinTypes,
			ASTModel<BUILTINTYPE, COMPLEXTYPE> astModel) {
		
		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(astModel);

		this.codeMap = codeMap;
		this.astModel = astModel;
		this.nameToTypeNoMap = new NameToTypeNoMap();
		this.scopedNameMap = new HashMap<>();
		this.methodMapCache = new MethodMapCache();
		
		// Add all builtin types
		for (BUILTINTYPE builtinType : builtinTypes) {
			
			final ResolvedType<BUILTINTYPE, COMPLEXTYPE> resolvedBuiltinType = new ResolvedTypeBuiltin<>(
					builtinType,
					astModel.getBuiltinTypeName(builtinType));
			
			addBuiltinType(resolvedBuiltinType);
		}
	}

	public int addFile(ResolvedFile<BUILTINTYPE, COMPLEXTYPE> file, int [] types) {
		return codeMap.addFile(types);
	}

	private int [] getExtendsFrom(ResolvedType<BUILTINTYPE, COMPLEXTYPE> type, TypeVariant typeVariant) {
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(typeVariant);
		
		final int [] extendsFrom;
		
		if (type.getExtendsFrom() != null) {
			
			int numExtendsFrom = 0;
			
			for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE> typeDependency : type.getExtendsFrom()) {
				if (typeDependency.getTypeVariant().equals(typeVariant)) {
					++ numExtendsFrom;
				}
			}
			
			extendsFrom = new int[numExtendsFrom];
			
			int dstIdx = 0;
			
			for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE> typeDependency : type.getExtendsFrom()) {
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

	private int addBuiltinType(ResolvedType<BUILTINTYPE, COMPLEXTYPE> type) {
		return addType(type, astModel.getBuiltinTypeName(type.getBuiltinType()), 0, null, null);
	}

	public int addType(ResolvedType<BUILTINTYPE, COMPLEXTYPE> type) {
		
		return addType(
				type,
				type.getTypeName(),
				astModel.getNumMethods(type.getType()),
				getExtendsFrom(type, TypeVariant.CLASS),
				getExtendsFrom(type, TypeVariant.INTERFACE));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int addType(ResolvedType<?, ?> type, TypeName typeName, int numMethods, int [] classesExtendsFrom, int [] interfacesExtendsFrom) {
		final int typeNo = codeMap.addType(
				type.getTypeVariant(),
				numMethods,
				classesExtendsFrom,
				interfacesExtendsFrom);

		this.resolvedTypes = allocateArray(
				this.resolvedTypes,
				typeNo + 1,
				length -> new ResolvedType[length]);
		
		resolvedTypes[typeNo] = (ResolvedType)type;
		
		nameToTypeNoMap.addMapping(typeName, typeNo);
		scopedNameMap.put(type.getScopedName(), typeNo);
		
		return typeNo;
	}
	
	public ResolvedType<BUILTINTYPE, COMPLEXTYPE> getType(TypeName completeName) {
		final int typeNo = getTypeNo(completeName);
		
		return resolvedTypes[typeNo];
	}
	
	public ResolvedType<BUILTINTYPE, COMPLEXTYPE> getType(ScopedName scopedName) {
		
		final Integer typeNo = scopedNameMap.get(scopedName);
		
		return typeNo != null ? resolvedTypes[typeNo] : null;
	}

	private TypeInfo getTypeInfo(int typeNo) {
		return new TypeInfo(typeNo, codeMap.getTypeVariant(typeNo));
	}

	@Override
	public COMPLEXTYPE getType(int typeNo) {
		return resolvedTypes[typeNo].getType();
	}

	@Override
	public TypeInfo getClassExtendsFromTypeInfo(TypeName classType) {
		final int type = codeMap.getClassThisExtendsFrom(getTypeNo(classType));

		return type < 0 ? null : getTypeInfo(type);
	}

	@Override
	public ResolvedType<BUILTINTYPE, COMPLEXTYPE> getClassThisExtendsFrom(TypeName classType) {

		final int type = codeMap.getClassThisExtendsFrom(getTypeNo(classType));
		
		return type < 0 ? null : resolvedTypes[type];
	}
	

	@Override
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getInterfacesImplement(TypeName classType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getInterfacesExtendFrom(TypeName interfaceType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean hasType(TypeName typeName) {
		return nameToTypeNoMap.getType(typeName) != null;
	}

	public Integer getTypeNo(TypeName type) {
		
		Objects.requireNonNull(type);
		
		return nameToTypeNoMap.getType(type);
	}
	
	public int addMethod(int type, String name, TypeName [] parameterTypes, MethodVariant methodVariant, int indexInType) {
		
		final int [] parameterTypeNos = new int[parameterTypes.length];
		
		for (int i = 0; i < parameterTypes.length; ++ i) {
			parameterTypeNos[i] = getTypeNo(parameterTypes[i]);
		}
		
		return codeMap.addMethod(type, name, parameterTypeNos, methodVariant, indexInType);
	}

	public void computeMethodExtends(TypeName completeName) {
		codeMap.computeMethodExtends(getTypeNo(completeName));
	}

	@Override
	public List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getDirectSubtypes(TypeName type) {
		
		Objects.requireNonNull(type);
		
		final int typeNo = getTypeNo(type);
		
		final int [] types = codeMap.getDirectSubtypes(typeNo);
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> result = new ArrayList<>(types.length);
		
		for (int resultTypeNo : types) {
			result.add(resolvedTypes[resultTypeNo]);
		}

		return result;
	}

	@Override
	public List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getAllSubtypes(TypeName type) {
		
		Objects.requireNonNull(type);
		
		final int typeNo = getTypeNo(type);
		
		final int [] types = codeMap.getAllSubtypes(typeNo);
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> result = new ArrayList<>(types.length);
		
		for (int resultTypeNo : types) {
			result.add(resolvedTypes[resultTypeNo]);
		}

		return result;
	}
	
	@Override
	public TypeInfo getTypeInfo(TypeName type) {

		final Integer typeNo = getTypeNo(type);
		
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
	public MethodInfo getMethodInfo(TypeName type, String methodName, TypeName [] parameterTypes) {
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(methodName);
		
		final int typeNo = getTypeNo(type);
		
		final int [] parameterTypeNos;
		
		if (parameterTypes == null) {
			parameterTypeNos = null;
		}
		else {
			parameterTypeNos = new int[parameterTypes.length];
		
			for (int i = 0; i < parameterTypes.length; ++ i) {

				final TypeName typeName = parameterTypes[i];
				final Integer parameterTypeNo = getTypeNo(typeName);
				
				if (parameterTypeNo == null) {
					throw new IllegalStateException("No typeNo for type " + typeName);
				}
				
				parameterTypeNos[i] = parameterTypeNo;
			}
		}
		
		return codeMap.getMethodInfo(typeNo, methodName, parameterTypeNos, methodMapCache);
	}
}