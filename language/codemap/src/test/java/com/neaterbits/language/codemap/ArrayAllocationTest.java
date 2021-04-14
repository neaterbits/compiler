package com.neaterbits.language.codemap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.Test;

import com.neaterbits.language.codemap.ArrayAllocation;

public class ArrayAllocationTest {

	@Test
	public void testAllocationOfObjectArray() {

		Object [] array = null;

		Object [] reallocated;

		final Function<Integer, Object[]> allocate = length -> new Object[length];


		array = ArrayAllocation.allocateArray(array, 0, allocate);
		assertThat(array).isNotNull();
		assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);

		reallocated  = ArrayAllocation.allocateArray(array, ArrayAllocation.DEFAULT_LENGTH / 2, allocate);
		assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);
		assertThat(reallocated).isSameAs(array);

		array = reallocated;
		reallocated  = ArrayAllocation.allocateArray(array, ArrayAllocation.DEFAULT_LENGTH - 1, allocate);
		assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);
		assertThat(reallocated).isSameAs(array);

		try {
			ArrayAllocation.allocateArray(array, ArrayAllocation.DEFAULT_LENGTH + 1, allocate);

			fail("Sequential array so should throw exception");
		}
		catch (IllegalStateException ex) {

		}

		array = reallocated;
		reallocated  = ArrayAllocation.allocateArray(array, ArrayAllocation.DEFAULT_LENGTH, allocate);
		assertThat(reallocated.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH + ArrayAllocation.DEFAULT_LENGTH);
		assertThat(reallocated).isNotSameAs(array);
	}

	@Test
	public void testAllocationOfIntArray() {

	    int [] array = null;

	    array = ArrayAllocation.allocateIntArray(array, 1000);

	    assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);

	    array = ArrayAllocation.allocateIntArray(array, 500);
        assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);

	    try {
	        array = ArrayAllocation.allocateIntArray(array, ArrayAllocation.DEFAULT_LENGTH + 1);

	        fail("Expected exception for allocation above current size");
	    }
	    catch (IllegalStateException ex) {

	    }

        array = ArrayAllocation.allocateIntArray(array, ArrayAllocation.DEFAULT_LENGTH);
        assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH + ArrayAllocation.DEFAULT_LENGTH);
	}

    @Test
    public void testAllocationOfLongArray() {

        long [] array = null;

        array = ArrayAllocation.allocateLongArray(array, 1000);

        assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);

        array = ArrayAllocation.allocateLongArray(array, 500);
        assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);

        try {
            array = ArrayAllocation.allocateLongArray(array, ArrayAllocation.DEFAULT_LENGTH + 1);

            fail("Expected exception for allocation above current size");
        }
        catch (IllegalStateException ex) {

        }

        array = ArrayAllocation.allocateLongArray(array, ArrayAllocation.DEFAULT_LENGTH);
        assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH + ArrayAllocation.DEFAULT_LENGTH);
    }

	@Test
	public void testAllocationOfIntArrayWithSubIntArray() {

		int [][] array = null;

		int [][] reallocated;

		array = ArrayAllocation.allocateIntArray(array, 0);
		assertThat(array).isNotNull();
		assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);

		reallocated  = ArrayAllocation.allocateIntArray(array, ArrayAllocation.DEFAULT_LENGTH / 2);
		assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);
		assertThat(reallocated).isSameAs(array);

		array = reallocated;
		reallocated  = ArrayAllocation.allocateIntArray(array, ArrayAllocation.DEFAULT_LENGTH - 1);
		assertThat(array.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH);
		assertThat(reallocated).isSameAs(array);

		try {
			ArrayAllocation.allocateIntArray(array, ArrayAllocation.DEFAULT_LENGTH + 1);

			fail("Sequential array so should throw exception");
		}
		catch (IllegalStateException ex) {

		}

		array = reallocated;
		reallocated  = ArrayAllocation.allocateIntArray(array, ArrayAllocation.DEFAULT_LENGTH);
		assertThat(reallocated.length).isEqualTo(ArrayAllocation.DEFAULT_LENGTH + ArrayAllocation.DEFAULT_LENGTH);
		assertThat(reallocated).isNotSameAs(array);
	}

	@Test
	public void testSubIntArrayValues() {

	    int [][] array = null;

	    array = ArrayAllocation.allocateIntArray(array, 10000);

	    final int initialSize = 1000;

	    for (int i = 0; i < initialSize; ++ i) {
	        ArrayAllocation.addToSubIntArray(array, 0, i, initialSize);
	    }

	    assertThat(ArrayAllocation.subIntArraySize(array, 0)).isEqualTo(initialSize);

	    final int [] values = ArrayAllocation.subIntArrayValues(array, 0);

	    for (int i = 0; i < initialSize; ++ i) {
	        assertThat(ArrayAllocation.subIntArrayValue(array[0], i)).isEqualTo(i);
	    }

	    assertThat(values.length).isEqualTo(initialSize);

	    for (int i = 0; i < initialSize; ++ i) {
	        assertThat(values[i]).isEqualTo(i);
	    }

	    assertThat(ArrayAllocation.subIntArrayInitialIndex(array[0])).isEqualTo(1);
        assertThat(ArrayAllocation.subIntArrayLastIndex(array[0])).isEqualTo(initialSize);

        // Allocate beyond initial size
        ArrayAllocation.addToSubIntArray(array, 0, 1000, initialSize);

        assertThat(ArrayAllocation.subIntArrayValue(array[0], 1000)).isEqualTo(1000);

        // Remove every other entry
        for (int i = 0; i <= 1000; ++ i) {
            if (i %2 == 0) {
                ArrayAllocation.removeDistinctFromSubIntArray(array, 0, i);
            }
        }

        assertThat(ArrayAllocation.subIntArraySize(array, 0)).isEqualTo(500);

        for (int i = 0; i < 500; ++ i) {
            assertThat(ArrayAllocation.subIntArrayValue(array[0], i)).isEqualTo(i * 2 + 1);
        }
    }

	@Test
	public void testPrintArray() {

        int [][] array = null;

        array = ArrayAllocation.allocateIntArray(array, 10000);

        ArrayAllocation.addToSubIntArray(array, 0, 35, 1000);
        ArrayAllocation.addToSubIntArray(array, 0, 58, 1000);
        ArrayAllocation.addToSubIntArray(array, 0, 62, 1000);
        ArrayAllocation.addToSubIntArray(array, 1, 34, 1000);

        final String string = ArrayAllocation.arrayToString(array, 3);


        assertThat(string).isEqualTo("   0 [35, 58, 62]\n   1 [34]\n   2 null\n");
	}
}
