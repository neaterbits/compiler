package com.neaterbits.compiler.codemap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.Test;

import com.neaterbits.compiler.codemap.ArrayAllocation;

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
}
