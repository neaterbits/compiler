package com.neaterbits.compiler.common.resolver.references;

import java.util.Arrays;

import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.TypeVariant;

abstract class BaseReferences {

	final int [] allocateIntArray(int [] array, int numEntries) {
		
		final int [] result;
		
		if (array == null) {
			result = new int[10000];
		}
		else if (array.length > numEntries) {
			result = array;
		}
		else if (array.length == numEntries) {
			result = Arrays.copyOf(array, array.length);
		}
		else {
			throw new IllegalStateException();
		}

		return result;
	}
	
	
	final int [][] allocateIntArray(int [][] array, int numEntries) {
		
		final int [][] result;
		
		if (array == null) {
			result = new int[10000][];
		}
		else if (array.length > numEntries) {
			result = array;
		}
		else if (array.length == numEntries) {
			result = Arrays.copyOf(array, array.length);
		}
		else {
			throw new IllegalStateException();
		}

		return result;
	}

	@FunctionalInterface
	interface TypeTest {
		boolean onTypeNoEncoded(int typeNoEncoded);
	}
	
	@FunctionalInterface
	interface GetCompiledType {
		CompiledType getCompiledType(int typeNo);
	}

	private static final int TYPEVARIANT_BITS = 2;
	private static final int TYPEVARIANT_MASK = ((1 << TYPEVARIANT_BITS) - 1) << (32 - TYPEVARIANT_BITS);
	
	static {
		if (TypeVariant.values().length >= (1 << TYPEVARIANT_BITS)) {
			throw new IllegalStateException("More bits required for type variant");
		}
	}
	
	static int encodeType(int typeNo, TypeVariant typeVariant) {
		return typeVariant.ordinal() << 30 | typeNo;
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
	
	static int decodeTypeNo(int encodedTypeNo) {
		return encodedTypeNo & (~TYPEVARIANT_MASK);
	}
}
