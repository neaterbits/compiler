package dev.nimbler.ide.util.ui.text;

public class CharArray64Bit extends Array64Bit<char[][], char[]>{

	public CharArray64Bit(long initialCapacity, int subArrayInitialCapacity) {
		super(initialCapacity, subArrayInitialCapacity, char[][]::new);
	}

	public CharArray64Bit(long initialCapacity, int subArrayInitialCapacity, int maxArraySize) {
		super(initialCapacity, subArrayInitialCapacity, maxArraySize, char[][]::new);
	}

	protected CharArray64Bit(CharArray64Bit toCopy) {
		super(toCopy);
	}
	
	@Override
	final int getArraysLength(char[][] arrays) {
		return arrays.length;
	}

	@Override
	final int getSubArrayLength(char[] subArray) {
		return subArray.length;
	}

	@Override
	final char[] getSubArray(char[][] arrays, int index) {
		return arrays[index];
	}

	@Override
	final void setSubArray(char[][] arrays, int index, char[] subArray) {
		arrays[index] = subArray;
	}

	@Override
	final char[] createSubArray(int length) {
		return new char[length];
	}
}
