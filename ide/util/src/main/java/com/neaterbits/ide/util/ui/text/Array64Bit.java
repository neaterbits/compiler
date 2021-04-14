package com.neaterbits.ide.util.ui.text;

import java.util.function.Function;

abstract class Array64Bit<ARRAYS, SUBARRAY> {

	final int maxArraySize;
	
	ARRAYS arrays;
	private final int subArrayInitialCapacity;
	private final Function<Integer, ARRAYS> createArrays;
	
	private long length;

	abstract int getArraysLength(ARRAYS arrays);
	
	abstract int getSubArrayLength(SUBARRAY subArray);
	
	abstract SUBARRAY getSubArray(ARRAYS arrays, int index);

	abstract void setSubArray(ARRAYS arrays, int index, SUBARRAY subArray);
	
	abstract SUBARRAY createSubArray(int length);
	
	Array64Bit(long initialCapacity, int subArrayInitialCapacity, Function<Integer, ARRAYS> createArrays) {
		this(initialCapacity, subArrayInitialCapacity, Integer.MAX_VALUE, createArrays);
	}
	
	Array64Bit(long initialCapacity, int subArrayInitialCapacity, int maxArraySize, Function<Integer, ARRAYS> createArrays) {
	
		this.subArrayInitialCapacity = subArrayInitialCapacity;
		this.maxArraySize = maxArraySize;
		this.createArrays = createArrays;
		
		long remaining = initialCapacity;
		
		int idx = 0;

		final int numArrays = getArraysNumEntries(remaining);

		this.arrays = createArrays.apply(numArrays);
		
		if (arrays == null) {
			throw new IllegalStateException();
		}
		
		while (remaining != 0) {

			final long length = Math.min(remaining, maxArraySize);
			
			setSubArray(arrays, idx ++, createSubArray((int)length));
			
			remaining -= length;
		}
	}

	protected Array64Bit(Array64Bit<ARRAYS, SUBARRAY> toCopy) {
		this.subArrayInitialCapacity = toCopy.subArrayInitialCapacity;
		this.maxArraySize = toCopy.maxArraySize;
		this.createArrays = toCopy.createArrays;
	
		this.length = toCopy.length;
		
		final int arraysLength = toCopy.getArraysLength(toCopy.arrays);
		
		this.arrays = createArrays.apply(arraysLength);
		
		for (int i = 0; i < arraysLength; ++ i) {
			final SUBARRAY subArray = toCopy.getSubArray(toCopy.arrays, i);
			
			setSubArray(arrays, i, copyOfSubArray(subArray, toCopy.getSubArrayLength(subArray)));
		}
	}
	
	public final long getLength() {
		return length;
	}

	static final int getArraysNumEntries(long length, int maxArraySize) {
		return (int)((length - 1) / maxArraySize) + 1;
	}

	final int getArraysNumEntries(long length) {
		return getArraysNumEntries(length, maxArraySize);
	}

	final int getArraysIndex(long index) {
		final long arraysIdx = index / maxArraySize;
		
		return (int)arraysIdx;
	}
	
	final int getSubArrayIndex(long index) {
		return (int)(index % maxArraySize);
	}

	private int computeSubArrayAllocLength(long subArrayIdx) {
		
		if (subArrayIdx > maxArraySize) {
			throw new IllegalArgumentException();
		}
		
		final long length = subArrayIdx >= subArrayInitialCapacity
				? subArrayIdx * 4
				: subArrayInitialCapacity;
		
		return (int)Math.min(length, maxArraySize);
	}

	private ARRAYS copyOfArrays(ARRAYS arrays, int newLength) {
		
		final ARRAYS newArrays = createArrays.apply(newLength);
		
		System.arraycopy(arrays, 0, newArrays, 0, getArraysLength(arrays));
		
		return newArrays;
	}

	private SUBARRAY copyOfSubArray(SUBARRAY subArray, int newLength) {
		
		final SUBARRAY newArray = createSubArray(newLength);
		
		System.arraycopy(subArray, 0, newArray, 0, getSubArrayLength(subArray));
		
		return newArray;
	}

	final long prepareArraysForSet(long idx) {

		final int arraysIdx = (int)(idx / maxArraySize);
		final int subArrayIdx = (int)(idx % maxArraySize);
		
		if (arraysIdx >= getArraysLength(arrays)) {
			
			this.arrays = copyOfArrays(arrays, getArraysLength(arrays) * 4);
		}
		
		if (getSubArray(arrays, arraysIdx) == null) {
			setSubArray(arrays, arraysIdx, createSubArray(computeSubArrayAllocLength(subArrayIdx)));
		}
		else if (subArrayIdx >= getSubArrayLength(getSubArray(arrays, arraysIdx))) {
			
			// System.out.println("## reallocating " + subArrayIdx + " " + getSubArrayLength(subArrayIdx));
			setSubArray(arrays, arraysIdx, copyOfSubArray(getSubArray(arrays, arraysIdx), computeSubArrayAllocLength(subArrayIdx)));
		}
		
		if (idx >= length) {
			length = idx + 1;
		}
		
		final long aIdx = arraysIdx;
		final long saIdx = subArrayIdx;
		
		return aIdx << 32 | saIdx;
	}
}
