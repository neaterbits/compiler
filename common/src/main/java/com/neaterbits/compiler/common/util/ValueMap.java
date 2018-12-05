package com.neaterbits.compiler.common.util;

public final class ValueMap {

	private final int bitsPerValue;
	private final int valueCount;
	
	private final long [] values;

	public ValueMap(int bitsPerValue, int valueCount) {
		this.bitsPerValue = bitsPerValue;
		this.valueCount = valueCount;
		
		final int numLongs = (((bitsPerValue * valueCount) + 64) / 64) - 1;
		
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
		
		final int bitOffset = index * bitsPerValue + offset;

		final int arrayIndex = bitOffset / 64;
		final int bitOffsetInLong = bitOffset % 64;
		
		final int spaceInLong = 64 - bitOffsetInLong;
	
		long result;
		
		if (numBits <= spaceInLong) {
			result = (values[arrayIndex] >> bitOffsetInLong) & Bits.mask(numBits);
		}
		else {
			result = values[arrayIndex] >> bitOffsetInLong;
			
			final int spaceInNextLong = numBits - spaceInLong;
			
			result |= values[arrayIndex + 1] & Bits.mask(spaceInNextLong);
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
		
		if (index >= valueCount) {
			throw new IllegalArgumentException();
		}
		
		if (value > Bits.mask(numBits)) {
			throw new IllegalArgumentException();
		}
		
		final int bitOffset = index * bitsPerValue + offset;

		final int arrayIndex = bitOffset / 64;
		final int bitOffsetInLong = bitOffset % 64;
		
		final int spaceInLong = 64 - bitOffsetInLong;
	
		
		if (numBits <= spaceInLong) {
			values[arrayIndex] |= value << bitOffsetInLong;
		}
		else {
			values[arrayIndex] |= (value & Bits.mask(spaceInLong)) << bitOffsetInLong;
			values[arrayIndex + 1] |= value >> spaceInLong;
		}
	}
}
