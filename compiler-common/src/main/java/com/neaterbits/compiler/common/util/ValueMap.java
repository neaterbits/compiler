package com.neaterbits.compiler.common.util;

public final class ValueMap {

	private final int bitsPerValue;
	private final int valueCount;
	
	private final long [] values;

	static int getAllocationSize(int bitsPerValue, int valueCount) {
		
		final int numBitsTotal = bitsPerValue * valueCount;
		
		return ((numBitsTotal - 1) / 64) + 1;
	}
	
	public ValueMap(int bitsPerValue, int valueCount) {
		this.bitsPerValue = bitsPerValue;
		this.valueCount = valueCount;
		
		final int numLongs = getAllocationSize(bitsPerValue, valueCount);
		
		this.values = new long[numLongs];
	}
	
	public long getValue(int index) {
		
		if (bitsPerValue > 64) {
			throw new IllegalStateException();
		}
	
		return getValue(index, 0, bitsPerValue);
	}
	
	public long getValue(int index, int offset, int numBits) {
		
		if (numBits > 64) {
			throw new IllegalArgumentException();
		}
		
		if (index >= valueCount) {
			throw new IllegalArgumentException();
		}
		
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		
		final int bitOffset = index * bitsPerValue + offset;

		final int arrayIndex = bitOffset / 64;
		final int bitOffsetInLong = bitOffset % 64;
		
		final int spaceInLong = 64 - bitOffsetInLong;
	
		long result;
		
		if (numBits <= spaceInLong) {
			result = ((values[arrayIndex] >> bitOffsetInLong) & Bits.maskForNumBits(numBits));
		}
		else {
			result = values[arrayIndex] >> bitOffsetInLong;
			
			final int spaceInNextLong = numBits - spaceInLong;
			
			result |= values[arrayIndex + 1] & Bits.maskForNumBits(spaceInNextLong);
		}

		return result;
	}
	
	public void storeValue(int index, long value) {
		
		if (bitsPerValue > 64) {
			throw new IllegalStateException();
		}

		storeValue(index, 0, bitsPerValue, value);
	}

	public void storeValue(int index, int offset, int numBits, long value) {
		
		if (numBits > 64) {
			throw new IllegalArgumentException();
		}
		
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		
		if (index >= valueCount) {
			throw new IllegalArgumentException();
		}
		
		if (numBits < 64 && value > Bits.maskForNumBits(numBits)) {
			throw new IllegalArgumentException("value " + value + " > mask " + Bits.maskForNumBits(numBits) + " for " + numBits);
		}
		
		final int bitOffset = index * bitsPerValue + offset;

		final int arrayIndex = bitOffset / 64;
		final int bitOffsetInLong = bitOffset % 64;
		
		final int spaceInLong = 64 - bitOffsetInLong;
		
		if (numBits <= spaceInLong) {
			values[arrayIndex] &= ~(Bits.mask(numBits, bitOffsetInLong));
			values[arrayIndex] |= value << bitOffsetInLong;
		}
		else {
			values[arrayIndex] |= (value & Bits.maskForNumBits(spaceInLong)) << bitOffsetInLong;
			values[arrayIndex + 1] |= value >> spaceInLong;
		}
	}
}
