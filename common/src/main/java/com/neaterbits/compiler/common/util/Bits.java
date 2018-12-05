package com.neaterbits.compiler.common.util;

public class Bits {
	
	public static long mask(int numBits) {
		return (1 << (numBits + 1)) - 1;
	}

	public static long mask(int numBits, int shift) {
		return mask(numBits) << shift;
	}

	public static int getNumBitsForStoringMaxValue(long value) {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		
		for (int i = 63; i >= 0; -- i) {
			if ((value & (1L << i)) != 0L) {
				return i;
			}
		}
		
		return 0;
	}
}
