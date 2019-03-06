package com.neaterbits.compiler.common.util;

public class Bits {
	
	public static long maskForNumBits(int numBits) {
		
		if (numBits < 0) {
			throw new IllegalArgumentException();
		}
		
		if (numBits > 64) {
			throw new IllegalArgumentException();
		}
		
		return numBits == 64 ? 0xFFFFFFFFFFFFFFFFL : (1L << (numBits)) - 1;
	}

	public static long mask(int numBits, int shift) {
		return maskForNumBits(numBits) << shift;
	}

	public static int getNumBitsForStoringMaxValue(long value) {
		
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		
		// Find first bit set from MSB
		for (int i = 63; i >= 0; -- i) {
			
			final long mask = 1L << i;
			
			if ((value & mask) != 0L) {
				return i + 1;
			}
		}
		
		return 0;
	}
}
