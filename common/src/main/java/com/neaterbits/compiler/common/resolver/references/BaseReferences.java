package com.neaterbits.compiler.common.resolver.references;

import java.util.Arrays;
import java.util.function.Function;

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

	final long [] allocateLongArray(long [] array, int numEntries) {
		
		final long [] result;
		
		if (array == null) {
			result = new long[10000];
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

	final <T> T [] allocateArray(T [] array, int numEntries, Function<Integer, T[]> createArray) {
		
		final T [] result;
		
		if (array == null) {
			result = createArray.apply(10000);
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
		return allocateIntArray(array, numEntries, true);
	}
	
	final int [][] allocateIntArray(int [][] array, int numEntries, boolean sequential) {
		
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
		else if (!sequential) {
			result = Arrays.copyOf(array, numEntries + 10000);
		}
		else {
			throw new IllegalStateException();
		}

		return result;
	}

	final void addToSubIntArray(int [][] array, int primaryIndex, int value, int initialSize) {
		int [] subArray = array[primaryIndex];
		
		if (subArray == null) {
			subArray = array[primaryIndex] = new int[initialSize + 1];
		}

		final int numEntries = subArray[0];

		if (numEntries + 1 + 1> subArray.length) {
			array[primaryIndex] = subArray = Arrays.copyOf(subArray, subArray.length * 2);
		}

		subArray[numEntries + 1] = value;

		++ subArray[0];
	}

	final int subIntArraySize(int [][] array, int index) {
		return array[index][0];
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
	
	static int encodeTypeVariant(int index, TypeVariant typeVariant) {
		return typeVariant.ordinal() << 30 | index;
	}

	static int encodeType(int typeNo, TypeVariant typeVariant) {
		return encodeTypeVariant(typeNo, typeVariant);
	}

	static int encodeMethod(int methodNo, TypeVariant typeVariant) {
		return encodeTypeVariant(methodNo, typeVariant);
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

	static int decodeMethodNo(int encodedTypeNo) {
		return decodeIndex(encodedTypeNo);
	}
}
