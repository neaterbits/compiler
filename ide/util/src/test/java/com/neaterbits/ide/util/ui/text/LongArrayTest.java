package com.neaterbits.ide.util.ui.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LongArrayTest {

	@Test
	public void testLongArray() {
		
		final int max = 1 << 12;
		
		final long length = (((long)max) * 3) + 120000;

		final LongArray longArray = new LongArray(10000, 10000, max);

		for (long i = 0; i < length; ++ i) {
			
			final long value = i + 3;
			
			longArray.set(i, value);
			
			assertThat(longArray.get(i)).isEqualTo(value);
			
			assertThat(longArray.getLength()).isEqualTo(i + 1);
		}
		
		for (long i = 0; i < length; ++ i) {
			assertThat(longArray.binarySearchForPrevious(i + 3)).isEqualTo(i);
		}
	}
	
	@Test
	public void testLongArrayBinarySearch() {
		
		final long [] array = new long [] {
				1, 2, 3, 4, 5, 6
		};
		
		assertThat(LongArray.binarySearch(array, array.length, 0)).isEqualTo(-1);
		assertThat(LongArray.binarySearch(array, array.length, 1)).isEqualTo(0);
		assertThat(LongArray.binarySearch(array, array.length, 2)).isEqualTo(1);
		assertThat(LongArray.binarySearch(array, array.length, 3)).isEqualTo(2);
		assertThat(LongArray.binarySearch(array, array.length, 4)).isEqualTo(3);
		assertThat(LongArray.binarySearch(array, array.length, 5)).isEqualTo(4);
		assertThat(LongArray.binarySearch(array, array.length, 6)).isEqualTo(5);
		assertThat(LongArray.binarySearch(array, array.length, 7)).isEqualTo(5);
	}
}
