package dev.nimbler.language.codemap;

import java.util.Arrays;
import java.util.function.Function;

public final class ArrayAllocation {

	public static final int DEFAULT_LENGTH = 10000;

	public static final int [] allocateIntArray(int [] array, int numEntries) {

		final int [] result;

		if (array == null) {
			result = new int[DEFAULT_LENGTH];
		}
		else if (array.length > numEntries) {
			result = array;
		}
		else if (array.length == numEntries) {
			result = Arrays.copyOf(array, array.length + DEFAULT_LENGTH);
		}
		else {
			throw new IllegalStateException();
		}

		return result;
	}

	public static long [] allocateLongArray(long [] array, int numEntries) {

		final long [] result;

		if (array == null) {
			result = new long[DEFAULT_LENGTH];
		}
		else if (array.length > numEntries) {
			result = array;
		}
		else if (array.length == numEntries) {
			result = Arrays.copyOf(array, array.length + DEFAULT_LENGTH);
		}
		else {
			throw new IllegalStateException();
		}

		return result;
	}

	public static <T> T [] allocateArray(T [] array, int numEntries, Function<Integer, T[]> createArray) {

		final T [] result;

		if (array == null) {
			result = createArray.apply(DEFAULT_LENGTH);
		}
		else if (array.length > numEntries) {
			result = array;
		}
		else if (array.length == numEntries) {
			result = Arrays.copyOf(array, array.length + DEFAULT_LENGTH);
		}
		else {
			throw new IllegalStateException();
		}

		return result;
	}

	public static int [][] allocateIntArray(int [][] array, int numEntries) {
		return allocateIntArray(array, numEntries, true);
	}

	static int [][] allocateIntArray(int [][] array, int numEntries, boolean sequential) {

		final int [][] result;

		if (array == null) {

			if (numEntries > DEFAULT_LENGTH) {
				throw new IllegalStateException();
			}

			result = new int[DEFAULT_LENGTH][];
		}
		else if (array.length > numEntries) {
			result = array;
		}
		else if (array.length == numEntries) {
			result = Arrays.copyOf(array, array.length + DEFAULT_LENGTH);
		}
		else if (!sequential) {
			result = Arrays.copyOf(array, numEntries + DEFAULT_LENGTH);
		}
		else {
			throw new IllegalStateException("array.length=" + array.length + ", numEntries=" + numEntries + ", sequential=" + sequential);
		}

		return result;
	}

	public static void addToSubIntArray(int [][] array, int primaryIndex, int value, int initialSize) {
		int [] subArray = array[primaryIndex];

		if (subArray == null) {
			subArray = array[primaryIndex] = allocateSubArray(initialSize);
		}

		final int numEntries = subArray[0];

		if (numEntries + 1 + 1 > subArray.length) {
			array[primaryIndex] = subArray = Arrays.copyOf(subArray, subArray.length * 2);
		}

		subArray[numEntries + 1] = value;

		++ subArray[0];
	}

    public static void removeDistinctFromSubIntArray(int [][] array, int primaryIndex, int toRemove) {

        array[primaryIndex] = removeDistinctFromSubIntArray(array[primaryIndex], toRemove);
    }

    private static int [] removeDistinctFromSubIntArray(int [] array, int toRemove) {

        final int numEntries = array[0];

        if (numEntries <= 0) {
            throw new IllegalArgumentException();
        }

        final int [] updated = new int[array.length - 1];

        int dstIdx = 1;

        boolean removed = false;

        for (int i = 1; i <= numEntries; ++ i) {

            if (dstIdx >= updated.length) {
                throw new IllegalStateException("Value not found");
            }

            final int toMove = array[i];

            if (toMove != toRemove) {
                updated[dstIdx ++] = toMove;
            }
            else {
                if (removed) {
                    throw new IllegalStateException();
                }

                removed = true;
            }
        }

        updated[0] = numEntries - 1;

        return updated;
    }

	static int [] allocateSubArray(int initialSize) {
		return new int[initialSize + 1];
	}

	public static int subIntArraySize(int [][] array, int index) {
		return subIntArraySize(array[index]);
	}

    static int subIntArraySize(int [] subArray) {
        return subArray[0];
    }

	public static int subIntArrayValue(int [] array, int index) {
		return array[1 + index];
	}

	public static int subIntArrayInitialIndex(int [] array) {
		return 1;
	}

	public static int subIntArrayLastIndex(int [] array) {
		return array[0];
	}

	public static int [] subIntArrayValues(int [][] array, int primaryIndex) {

	    return subIntArrayCopy(array[primaryIndex]);
	}

	public static int [] subIntArrayCopy(int [] array) {

		final int numEntries = array[0];

		final int [] result = new int[numEntries];

		for (int i = 0; i < numEntries; ++ i) {
			result[i] = array[i + 1];
		}

		return result;
	}

	static void subIntArrayPrint(int [] array, StringBuilder sb) {

		sb.append('[');

		for (int i = 0; i < array[0]; ++ i) {
			if (i > 0) {
				sb.append(", ");
			}

			sb.append(array[i + 1]);
		}

		sb.append(']');
	}

	static String arrayToString(int [][] array, int entries) {

		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < Math.min(entries, array.length); ++ i) {

			final int [] subArray = array[i];

			sb.append(String.format("%4d ", i));

			if (subArray != null) {
				subIntArrayPrint(subArray, sb);
			}
			else {
				sb.append("null");
			}

			sb.append('\n');
		}

		return sb.toString();
	}
}
