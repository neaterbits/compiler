package com.neaterbits.compiler.common.resolver.codemap;

import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.TypeVariant;

class Encode {
	private static final int TYPEVARIANT_BITS = 2;
	private static final int TYPEVARIANT_MASK = ((1 << TYPEVARIANT_BITS) - 1) << (32 - TYPEVARIANT_BITS);

	static final int TYPE_BITS = 20;
	static final int SIGNATURE_BITS = 20;
	static final int METHOD_VARIANT_BITS = 2;
	static final int METHOD_BITS = 22;
	
	static final int METHOD_AND_VARIANT_BITS = METHOD_BITS + METHOD_VARIANT_BITS;
	
	static final int MAX_TYPES 		= 1 << TYPE_BITS;
	static final int MAX_SIGNATURES = 1 << SIGNATURE_BITS;
	static final int MAX_METHODS 	= 1 << METHOD_BITS;
	
	static final int METHOD_MASK = (1 << METHOD_BITS) - 1;
	
	static final int PARAM_NAME_BITS  = 20;
	static final int PARAM_TYPES_BITS = 20;
	static final int SIGNATURENO_BITS = 24;
	
	static final int MAX_SIGNATURENO = (1 << SIGNATURENO_BITS);
	
	@FunctionalInterface
	interface TypeTest {
		boolean onTypeNoEncoded(int typeNoEncoded);
	}
	
	@FunctionalInterface
	interface GetCompiledType {
		CompiledType getCompiledType(int typeNo);
	}

	
	static {
		if (TypeVariant.values().length >= (1 << TYPEVARIANT_BITS)) {
			throw new IllegalStateException("More bits required for type variant");
		}
	}
	
	static int encodeTypeVariant(int index, TypeVariant typeVariant) {
		return typeVariant.ordinal() << 30 | index;
	}

	static int encodeType(int typeNo, TypeVariant typeVariant) {
		return encodeTypeVariant(typeNo, typeVariant);
	}

	static TypeVariant getTypeVariant(int encodedTypeNo) {
		return TypeVariant.values()[encodedTypeNo >>> 30];
	}
	
	static boolean isInterface(int encodedTypeNo) {
		return getTypeVariant(encodedTypeNo) == TypeVariant.INTERFACE;
	}

	static boolean isClass(int encodedTypeNo) {
		return getTypeVariant(encodedTypeNo) == TypeVariant.CLASS;
	}

	static boolean isEnum(int encodedTypeNo) {
		return getTypeVariant(encodedTypeNo) == TypeVariant.ENUM;
	}
	
	private static int decodeIndex(int encodedIndex) {
		return encodedIndex & (~TYPEVARIANT_MASK);
	}
	
	static int decodeTypeNo(int encodedTypeNo) {
		return decodeIndex(encodedTypeNo);
	}

	static int encodeMethod(int methodNo, TypeVariant typeVariant, MethodVariant methodVariant) {

		return encodeTypeVariant(methodVariant.ordinal() << METHOD_BITS | methodNo , typeVariant);
		
	}

	static int encodeMethodWithMethodVariant(int methodNoWihMethodVariant, TypeVariant typeVariant) {

		return encodeTypeVariant(methodNoWihMethodVariant, typeVariant);
		
	}

	static int encodeMethodWithoutTypeVariant(int methodNo, MethodVariant methodVariant) {
		return methodVariant.ordinal() << METHOD_BITS | methodNo;
	}

	static int decodeMethodNo(int encodedMethodNo) {
		return encodedMethodNo & METHOD_MASK;
	}
	
	static MethodVariant getMethodVariant(int encodedMethodNo) {
		return MethodVariant.values()[(encodedMethodNo >> METHOD_BITS) & ((1 << METHOD_VARIANT_BITS) - 1)];
	}
}
