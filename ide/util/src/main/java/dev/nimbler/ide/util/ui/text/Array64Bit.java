package dev.nimbler.ide.util.ui.text;

import java.util.function.Function;

/**
 * Base class for arrays with 64 bit indices, provides some helper methods.
 * Implemented with arrays of arrays.
 * 
 * @param <ARRAYS> arrays of arrays type, e.g. long[][]
 * @param <SUBARRAY> array of the type to store, e.g. long[] for storing a 64-bit array of longs
 */

abstract class Array64Bit<ARRAYS, SUBARRAY> {

	final int maxSubArraySize;
	
	ARRAYS arrays;
	private final int subArrayInitialCapacity;
	private final Function<Integer, ARRAYS> createArrays;
	
	private long length;

	/**
	 * Get length of arrays-of-arrays, necessary here since no generic method exists.
	 * 
	 * @param arrays the arrays-of-arrays object.
	 * 
	 * @return length of arrays-of-arrays object
	 */
	abstract int getArraysLength(ARRAYS arrays);
	
    /**
     * Get length of sub-array, necessary here since no generic method exists.
     * 
     * @param subArray the sub array object.
     * 
     * @return length of sub array object
     */
	abstract int getSubArrayLength(SUBARRAY subArray);
	
	abstract SUBARRAY getSubArray(ARRAYS arrays, int index);

	abstract void setSubArray(ARRAYS arrays, int index, SUBARRAY subArray);
	
	abstract SUBARRAY createSubArray(int length);
	
	abstract boolean subArrayEquals(SUBARRAY subArray1, SUBARRAY subArray2);
	
	/**
	 * Create a new 64 bit array.
	 * 
	 * @param initialCapacity the initial capacity of the overall array
	 * @param subArrayInitialCapacity initial capacity for each sub array allocated
	 * @param createArrays function to create arrays-of-arrays
	 */
	Array64Bit(long initialCapacity, int subArrayInitialCapacity, Function<Integer, ARRAYS> createArrays) {
		this(initialCapacity, subArrayInitialCapacity, Integer.MAX_VALUE, createArrays);
	}
	
    /**
     * Create a new 64 bit array.
     * 
     * @param initialCapacity the initial capacity of the overall array
     * @param subArrayInitialCapacity initial capacity for each sub array allocated
     * @param maxSubArraySize max size of each sub array
     * @param createArrays function to create arrays-of-arrays
     */
	Array64Bit(long initialCapacity, int subArrayInitialCapacity, int maxSubArraySize, Function<Integer, ARRAYS> createArrays) {
	
		this.subArrayInitialCapacity = subArrayInitialCapacity;
		this.maxSubArraySize = maxSubArraySize;
		this.createArrays = createArrays;
		
		long remaining = initialCapacity;
		
		int idx = 0;

		final int numArrays = getArraysNumEntries(remaining);

		this.arrays = createArrays.apply(numArrays);
		
		if (arrays == null) {
			throw new IllegalStateException();
		}
		
		while (remaining != 0) {

			final long length = Math.min(remaining, maxSubArraySize);
			
			setSubArray(arrays, idx ++, createSubArray((int)length));
			
			remaining -= length;
		}
	}

	protected Array64Bit(Array64Bit<ARRAYS, SUBARRAY> toCopy) {
		this.subArrayInitialCapacity = toCopy.subArrayInitialCapacity;
		this.maxSubArraySize = toCopy.maxSubArraySize;
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

	static final int getArraysNumEntries(long length, int maxSubArraySize) {
		return (int)((length - 1) / maxSubArraySize) + 1;
	}

	final int getArraysNumEntries(long length) {
		return getArraysNumEntries(length, maxSubArraySize);
	}

	final int getArraysIndex(long index) {
		final long arraysIdx = index / maxSubArraySize;
		
		return (int)arraysIdx;
	}
	
	final int getSubArrayIndex(long index) {
		return (int)(index % maxSubArraySize);
	}

	private int computeSubArrayAllocLength(long subArrayIdx) {
		
		if (subArrayIdx > maxSubArraySize) {
			throw new IllegalArgumentException();
		}
		
		final long length = subArrayIdx >= subArrayInitialCapacity
				? subArrayIdx * 4
				: subArrayInitialCapacity;
		
		return (int)Math.min(length, maxSubArraySize);
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

		final int arraysIdx = (int)(idx / maxSubArraySize);
		final int subArrayIdx = (int)(idx % maxSubArraySize);
		
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

    @Override
    public boolean equals(Object obj) {
    
        boolean equals;

        if (obj == null) {
            equals = false;
        }
        else if (!(obj instanceof Array64Bit)) {
            equals = false;
        }
        else {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            final Array64Bit<Object, Object> array = (Array64Bit)obj;

            if (getLength() != array.getLength()) {
                equals = false;
            }
            else {
                final int arraysLength = getArraysLength(arrays);

                if (arraysLength != array.getArraysLength(array.arrays)) {
                    equals = false;
                }
                else {
                    equals = true;
                    
                    for (int i = 0; i < arraysLength; ++ i) {
                    
                        final SUBARRAY sub1 = getSubArray(arrays, i);
                        final Object sub2Obj = array.getSubArray(array.arrays, i);
                        
                        @SuppressWarnings("unchecked")
                        final SUBARRAY sub2 = (SUBARRAY)sub2Obj;
                        
                        if (!sub1.getClass().equals(sub2Obj.getClass())) {
                            equals = false;
                            break;
                        }
                        
                        if (getSubArrayLength(sub1) != array.getSubArrayLength(sub2)) {
                            equals = false;
                            break;
                        }
                        
                        if (!subArrayEquals(sub1, (SUBARRAY)sub2)) {
                            equals = false;
                            break;
                        }
                    }
                }
            }
        }

        return equals;
    }
}
