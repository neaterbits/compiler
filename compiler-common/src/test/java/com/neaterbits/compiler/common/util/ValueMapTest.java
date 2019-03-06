package com.neaterbits.compiler.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

public class ValueMapTest {

	@Test
	public void testAllocationSize() {
		
		assertThat(ValueMap.getAllocationSize(1, 100)).isEqualTo(2);
	}

	@Test
	public void testValueMapOneBit() {
		
		final ValueMap valueMap = new ValueMap(1, 100);
		
		valueMap.storeValue(25, 1);
		
		assertThat(valueMap.getValue(0)).isEqualTo(0);
		assertThat(valueMap.getValue(25)).isEqualTo(1);
		assertThat(valueMap.getValue(75)).isEqualTo(0);
		
		try {
			valueMap.storeValue(100, 1);
			
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
			
		}
		
		try {
			valueMap.storeValue(-1, 1);
			
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
			
		}
		
		try {
			valueMap.storeValue(30, 2);
			
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
			
		}
	}
	
	@Test
	public void testValueMapOneTwoBits() {
		
		final ValueMap valueMap = new ValueMap(2, 150);
		
		valueMap.storeValue(25, 3);

		checkRange(valueMap, 0, 24, 0);
		
		assertThat(valueMap.getValue(25)).isEqualTo(3);

		checkRange(valueMap, 26, 149, 0);

		valueMap.storeValue(25, 2);

		checkRange(valueMap, 0, 24, 0);

		assertThat(valueMap.getValue(25)).isEqualTo(2);

		checkRange(valueMap, 26, 149, 0);
	}

	@Test
	public void testValueMapOneThreeBitsLongBoundary() {
		
		final ValueMap valueMap = new ValueMap(3, 150);
		
		valueMap.storeValue(25, 4);

		checkRange(valueMap, 0, 24, 0);
		
		assertThat(valueMap.getValue(25)).isEqualTo(4);

		checkRange(valueMap, 26, 149, 0);

		valueMap.storeValue(25, 2);

		checkRange(valueMap, 0, 24, 0);

		assertThat(valueMap.getValue(25)).isEqualTo(4);

		checkRange(valueMap, 26, 149, 0);
	}

	private void checkRange(ValueMap valueMap, int first, int last, int expected) {
		
		for (int i = first; i <= last; ++ i) {
			assertThat(valueMap.getValue(i)).isEqualTo(expected);
		}
	}
}
