package com.neaterbits.compiler.resolver;

import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.codemap.ResolvedTypeBuiltin;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.FieldInfo;
import com.neaterbits.compiler.util.model.MethodInfo;
import com.neaterbits.compiler.util.model.MethodVariant;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.Visibility;
import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMapGetters;
import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;

public final class ResolvedTypeCodeMapImpl<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		implements ResolvedTypeCodeMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	private final CompilerCodeMap codeMap;
	private final ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel;
	
	private ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> [] resolvedTypes;
	
	public ResolvedTypeCodeMapImpl(
			CompilerCodeMap codeMap,
			Collection<BUILTINTYPE> builtinTypes,
			ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel) {
		
		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(astModel);

		this.codeMap = codeMap;
		this.astModel = astModel;
		
		// Add all builtin types
		for (BUILTINTYPE builtinType : builtinTypes) {
			
			final ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedBuiltinType = new ResolvedTypeBuiltin<>(
					builtinType,
					astModel.getBuiltinTypeName(builtinType));
			
			addBuiltinType(resolvedBuiltinType);
		}
	}

	public int addNonResolvedFile(FileSpec fileSpec) {
		return codeMap.addFile(fileSpec.getDistinctName(), null);
	}

	public int addResolvedFile(ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> file, int [] types) {
		return codeMap.addFile(file.getSpec().getDistinctName(), types);
	}

	private int [] getExtendsFrom(ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> type, TypeVariant typeVariant) {
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(typeVariant);
		
		final int [] extendsFrom;
		
		if (type.getExtendsFrom() != null) {
			
			int numExtendsFrom = 0;
			
			for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typeDependency : type.getExtendsFrom()) {
				if (typeDependency.getTypeVariant().equals(typeVariant)) {
					++ numExtendsFrom;
				}
			}
			
			extendsFrom = new int[numExtendsFrom];
			
			int dstIdx = 0;
			
			for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typeDependency : type.getExtendsFrom()) {
				if (typeDependency.getTypeVariant().equals(typeVariant)) {

					final TypeName name = typeDependency.getCompleteName();
					
					if (name == null) {
						throw new IllegalStateException();
					}
					
					final Integer typeNo = codeMap.getTypeNoByTypeName(name);
					
					if (typeNo == null) {
						throw new IllegalStateException("No type no for name " + name + " from " + type.getTypeName());
					}
					
					extendsFrom[dstIdx ++] = typeNo;
				}
			}
		}
		else {
			extendsFrom = null;
		}
		
		return extendsFrom;
	}

	private int addBuiltinType(ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> type) {
		return addType(type, astModel.getBuiltinTypeName(type.getBuiltinType()), 0, null, null);
	}

	public int addType(ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> type) {
		
		return addType(
				type,
				type.getTypeName(),
				astModel.getNumMethods(type.getType()),
				getExtendsFrom(type, TypeVariant.CLASS),
				getExtendsFrom(type, TypeVariant.INTERFACE));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int addType(ResolvedType<?, ?, ?> type, TypeName typeName, int numMethods, int [] classesExtendsFrom, int [] interfacesExtendsFrom) {
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
		
		codeMap.addMapping(typeName, typeNo);
		
		return typeNo;
	}
	
	public ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> getType(TypeName completeName) {
		final int typeNo = getTypeNo(completeName);
		
		return resolvedTypes[typeNo];
	}
	
	private TypeInfo getTypeInfo(int typeNo) {
		return new TypeInfo(typeNo, codeMap.getTypeVariantForType(typeNo));
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
	public ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> getClassThisExtendsFrom(TypeName classType) {

		final int type = codeMap.getClassThisExtendsFrom(getTypeNo(classType));
		
		return type < 0 ? null : resolvedTypes[type];
	}
	

	@Override
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getInterfacesImplement(TypeName classType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getInterfacesExtendFrom(TypeName interfaceType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean hasType(TypeName typeName) {
		return codeMap.getTypeNoByTypeName(typeName) != null;
	}

	public Integer getTypeNo(TypeName type) {
		
		Objects.requireNonNull(type);
		
		return codeMap.getTypeNoByTypeName(type);
	}
	
	public int addField(
			int type,
			String name,
			TypeName fieldType,
			int numArrayDimensions,
			boolean isStatic,
			Visibility visibility,
			Mutability mutability,
			boolean isVolatile,
			boolean isTransient,
			int indexInType) {

		if (numArrayDimensions > 0) {
			throw new UnsupportedOperationException();
		}
		
		final int fieldTypeNo = codeMap.getTypeNoByTypeName(fieldType);
		if (fieldTypeNo < 0) {
			throw new IllegalArgumentException("Unknown type " + fieldType);
		}

		return codeMap.addField(type, name, fieldTypeNo, isStatic, visibility, mutability, isVolatile, isTransient, indexInType);
	}

	
	public int addMethod(int type, String name, TypeName [] parameterTypes, MethodVariant methodVariant, int indexInType) {
		
		final int [] parameterTypeNos = new int[parameterTypes.length];
		
		for (int i = 0; i < parameterTypes.length; ++ i) {
			parameterTypeNos[i] = getTypeNo(parameterTypes[i]);
		}
		
		return addMethod(type, name, parameterTypeNos, methodVariant, indexInType);
	}

	private int addMethod(int typeNo, String name, int [] parameterTypes, MethodVariant methodVariant, int indexInType) {
		return codeMap.addOrGetMethod(typeNo, name, methodVariant, -1, parameterTypes, indexInType);
	}

	public void computeMethodExtends(TypeName completeName) {
		codeMap.computeMethodExtends(getTypeNo(completeName));
	}

	@Override
	public List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getDirectExtendingThis(TypeName type) {
		
		Objects.requireNonNull(type);
		
		final int typeNo = getTypeNo(type);
		
		final int [] types = codeMap.getTypesDirectlyExtendingThis(typeNo);
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> result = new ArrayList<>(types.length);
		
		for (int resultTypeNo : types) {
			result.add(resolvedTypes[resultTypeNo]);
		}

		return result;
	}

	@Override
	public List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getAllSubtypes(TypeName type) {
		
		Objects.requireNonNull(type);
		
		final int typeNo = getTypeNo(type);
		
		final int [] types = codeMap.getAllTypesExtendingThis(typeNo);
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> result = new ArrayList<>(types.length);
		
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
	public FieldInfo getFieldInfo(TypeName type, String fieldName) {
		final Integer typeNo = getTypeNo(type);

		return typeNo != null ? codeMap.getFieldInfo(typeNo, fieldName) : null;
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
		
		return codeMap.getMethodInfo(typeNo, methodName, parameterTypeNos);
	}

	@Override
	public int addToken(int sourceFile, int parseTreeRef) {
		return codeMap.addToken(sourceFile, parseTreeRef);
	}
	
	@Override
	public void addTokenVariableReference(int fromToken, int toDeclarationToken) {
		codeMap.addTokenVariableReference(fromToken, toDeclarationToken);
	}

	public final CompilerCodeMapGetters getCompilerCodeMap() {
		return codeMap;
	}
	
	public final CrossReferenceUpdater getCrossReferenceUpdater() {
		return codeMap;
	}
}
