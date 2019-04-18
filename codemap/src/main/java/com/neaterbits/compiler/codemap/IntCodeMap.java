package com.neaterbits.compiler.codemap;

import static com.neaterbits.compiler.codemap.Encode.encodeType;

import com.neaterbits.compiler.util.Bits;
import com.neaterbits.compiler.util.ValueMap;

public class IntCodeMap implements CodeMap {

	private static final int NUM_METHOD_VARIANT_BITS = Bits.getNumBitsForStoringMaxValue(MethodVariant.values().length);
	
	private final TypeHierarchy typeHierarchy;

	private final MethodMap methodMap;
	private final MethodMapCache methodMapCache;
	
	private final MethodOverrideMap methodOverrideMap;
	
	private final ValueMap methodVariants;
	
	public IntCodeMap(MethodOverrideMap methodOverrideMap) {
		this.typeHierarchy 	= new TypeHierarchy();
		
		this.methodMap = new MethodMap();
		this.methodMapCache = new MethodMapCache();
		
		this.methodOverrideMap = methodOverrideMap;
		
		this.methodVariants = new ValueMap(NUM_METHOD_VARIANT_BITS, 10000);
	}

	@Override
	public int addType(TypeVariant typeVariant, int[] thisExtendsFromClasses, int [] thisExtendsFromInterfaces) {

		return typeHierarchy.addType(
				typeVariant,
				thisExtendsFromClasses != null ? typeHierarchy.encodeTypeVariant(thisExtendsFromClasses) : null,
				thisExtendsFromInterfaces != null ? typeHierarchy.encodeTypeVariant(thisExtendsFromInterfaces) : null
				);
	}

	private int getEncodedTypeNo(int typeNo) {
		return encodeType(typeNo, getTypeVariantForType(typeNo));
	}

	public void computeMethodExtends(int typeNo) {

		final int encodedTypeNo = getEncodedTypeNo(typeNo);
		
		final int [] extendedByEncoded = typeHierarchy.getTypesExtendingThisEncoded(typeNo);
		
		if (extendedByEncoded != null) {
			methodOverrideMap.addTypeExtendsTypes(encodedTypeNo, extendedByEncoded, methodMap);
		}
	}
	
	@Override
	public TypeVariant getTypeVariantForType(int typeNo) {
		return typeHierarchy.getTypeVariantForType(typeNo);
	}

	@Override
	public int getExtendsFromSingleSuperClass(int type) {
		return typeHierarchy.getExtendsFromSingleSuperClass(type);
	}

	private static final int [] EMPTY_ARRAY = new int[0];
	
	@Override
	public int[] getTypesThisDirectlyExtends(int typeNo) {
		final int [] encodedTypes = typeHierarchy.getTypesThisExtendsFromEncoded(typeNo);

		final int [] result;
		
		if (encodedTypes != null) {
			result = new int[encodedTypes.length];
			
			for (int i = 0; i < encodedTypes.length; ++ i) {
				result[i] = Encode.decodeTypeNo(encodedTypes[i]);
			}
		}
		else {
			result = EMPTY_ARRAY;
		}
		
		return result;
	}

	@Override
	public int[] getTypesDirectlyExtendingThis(int typeNo) {

		final int [] encodedTypes = typeHierarchy.getTypesExtendingThisEncoded(typeNo);

		final int [] result;
		
		if (encodedTypes != null) {
			result = new int[encodedTypes.length];
			
			for (int i = 0; i < encodedTypes.length; ++ i) {
				result[i] = Encode.decodeTypeNo(encodedTypes[i]);
			}
		}
		else {
			result = EMPTY_ARRAY;
		}
		
		return result;
	}

	@Override
	public void setMethodCount(int type, int methodCount) {
		methodMap.allocateMethods(type, methodCount);
	}

	@Override
	public int addOrGetMethod(int type, String methodName, MethodVariant methodVariant, int returnType, int [] parameters, int indexInType) {
		
		final int methodNo = methodMap.addMethod(
				type,
				typeHierarchy.getTypeVariantForType(type),
				methodName,
				parameters,
				methodVariant,
				indexInType,
				methodMapCache);
		
		methodVariants.storeValue(methodNo, methodVariant.ordinal());
		
		return methodNo;
	}

	@Override
	public int addOrGetExtendingMethod(
			int extendedMethod, MethodVariant extendedMethodVariant,
			int type,
			String methodName,
			MethodVariant methodVariant,
			int returnType,
			int[] parameters,
			int indexInType) {

		int methodNo = methodMap.getMethodNo(type, methodName, parameters, methodMapCache);
		
		if (methodNo < 0) {
			methodNo = addOrGetMethod(type, methodName, methodVariant, returnType, parameters, indexInType);
		}
		
		methodOverrideMap.addMethodExtends(
				extendedMethod, extendedMethodVariant,
				methodNo, methodVariant);
		
		return methodNo;
	}

	protected final MethodVariant getMethodVariant(int methodNo) {
		final long value = methodVariants.getValue(methodNo);
		
		return MethodVariant.values()[(int)value];
	}
	
	@Override
	public int getTypeForMethod(int methodNo) {
		return methodMap.getTypeForMethod(methodNo);
	}

	@Override
	public int getIndexForMethod(int methodNo) {
		return methodMap.getIndexForMethod(methodNo);
	}

	@Override
	public int getDistinctMethodCount(int typeNo, MethodFilter methodFilter, VTableScratchArea scratchArea) {
		return methodMap.getDistinctMethodCount(typeNo, methodFilter, typeHierarchy::getExtendsFromSingleSuperClass, scratchArea);
	}

	@Override
	public MethodInfo getMethodInfo(int typeNo, String methodName, int [] parameterTypes) {
		return methodMap.getMethodInfo(typeNo, methodName, parameterTypes, methodMapCache);
	}
	
	protected final int [] getMethodsDirectlyExtending(int methodNo) {
		return methodOverrideMap.getMethodsDirectlyExtending(methodNo);
	}
}
