package dev.nimbler.ide.util.ui.text;

import java.util.Arrays;

public final class LongArray extends Array64Bit<long[][], long[]> {
	
	public LongArray(long initialCapacity, int subArrayInitialCapacity) {
		super(initialCapacity, subArrayInitialCapacity, long[][]::new);
	}

	public LongArray(long initialCapacity, int subArrayInitialCapacity, int maxArraySize) {
		super(initialCapacity, subArrayInitialCapacity, maxArraySize, long[][]::new);
	}

	@Override
	int getArraysLength(long[][] arrays) {
		return arrays.length;
	}

	@Override
	long[] getSubArray(long[][] arrays, int index) {
		return arrays[index];
	}

	@Override
	int getSubArrayLength(long[] subArray) {
		return subArray.length;
	}

	@Override
	void setSubArray(long[][] arrays, int index, long[] subArray) {
		arrays[index] = subArray;
	}

	@Override
	long[] createSubArray(int length) {
		return new long[length];
	}

	@Override
    boolean subArrayEquals(long[] subArray1, long[] subArray2) {

        return Arrays.equals(subArray1, subArray2);
    }

	public long get(long idx) {
		return arrays[getArraysIndex(idx)][getSubArrayIndex(idx)];
	}
	
	public long binarySearchForPrevious(long value) {

		final int arraysIndex = binarySearch(
				arrays,
				getArraysNumEntries(getLength()),
				(long [] thisArray, long [] nextArray, boolean hasNextValue) -> {
			
			final int result;
			
			if (value < thisArray[0]) {
				result = -1;
			}
			else if (hasNextValue && value >= nextArray[0]) {
				result = 1;
			}
			else {
				result = 0;
			}
			
			return result;
		});
		
		final long [] subArray = arrays[arraysIndex];

		final int numArrays = (int)(((getLength() - 1) / maxSubArraySize) + 1);
		
		final int num = arraysIndex == numArrays - 1
				? (int)(getLength() % maxSubArraySize)
				: maxSubArraySize;
		
		final int subArrayIndex = binarySearch(subArray, num, value);
		
		return arraysIndex * maxSubArraySize + subArrayIndex;
	}
	
	@FunctionalInterface
	interface SearchComparator<T> {
		int compare(T value, T nextValue, boolean hasNextValue);
	}

	@FunctionalInterface
	interface LongSearchComparator {
		int compare(long value, long nextValue, boolean hasNextValue);
	}
	
	private static <T> int binarySearch(T [] array, int num, SearchComparator<T> comparator) {
		
		if (num > array.length) {
			throw new IllegalArgumentException();
		}
		
		return binarySearch(array, 0, num, comparator);
	}

	private static <T, V> int binarySearch(T [] array, int start, int num, SearchComparator<T> comparator) {
		
		int foundIndex;

		// System.out.println("## search start=" + start + ", num=" + num);

		if (num == 0) {
			foundIndex = -1;
		}
		else {

			final int sectionOffset = num / 2; 
			
			final int mid = start + sectionOffset;

			final boolean hasNextValue = num - sectionOffset > 1;
			final T nextValue = hasNextValue ? array[mid + 1] : null;
			
			// System.out.println("## compare to mid " + mid + " values " + array[mid] + ", " + nextValue);
			
			final int val = comparator.compare(array[mid], nextValue, hasNextValue);

			// System.out.println("## gives " + val);
			
			switch (val) {
			case -1:
				foundIndex = binarySearch(array, start, sectionOffset, comparator);
				break;
				
			case 0:
				foundIndex = mid;
				break;
				
			case 1:
				foundIndex = binarySearch(array, mid + 1, num - sectionOffset - 1, comparator);
				break;
	
			default:
				throw new UnsupportedOperationException();
			}
		}

		return foundIndex;
	}

	static <T> int binarySearch(long [] array, int num, long value) {
		
		return binarySearch(
				array,
				num,
				(lastValue, nextValue, hasNextValue) -> {
					
					final int result;
					
					if (value < lastValue) {
						result = -1;
					}
					else if (hasNextValue && value >= nextValue) {
						result = 1;
					}
					else {
						result = 0;
					}

					return result;
				});

	}
	
	private static <T> int binarySearch(long [] array, int num, LongSearchComparator comparator) {
		
		if (num > array.length) {
			throw new IllegalArgumentException();
		}

		return binarySearch(array, 0, num, comparator);
	}

	private static <T, V> int binarySearch(long [] array, int start, int num, LongSearchComparator comparator) {

		int foundIndex;

		// System.out.println("## search start=" + start + ", num=" + num);

		if (num == 0) {
			foundIndex = -1;
		}
		else {

			final int sectionOffset = num / 2; 
			
			final int mid = start + sectionOffset;

			final boolean hasNextValue = num - sectionOffset > 1;
			final long nextValue = hasNextValue ? array[mid + 1] : -1L;
			
			// System.out.println("## compare to mid " + mid + " values " + array[mid] + ", " + nextValue);
			
			final int val = comparator.compare(array[mid], nextValue, hasNextValue);

			// System.out.println("## gives " + val);
			
			switch (val) {
			case -1:
				foundIndex = binarySearch(array, start, sectionOffset, comparator);
				break;
				
			case 0:
				foundIndex = mid;
				break;
				
			case 1:
				foundIndex = binarySearch(array, mid + 1, num - sectionOffset - 1, comparator);
				break;
	
			default:
				throw new UnsupportedOperationException();
			}
		}

		return foundIndex;
	}

	public void set(long idx, long value) {

		final long indices = prepareArraysForSet(idx);
		
		arrays[(int)(indices >>> 32)][(int)(indices & 0xFFFFFFFFL)] = value;
	}
}
