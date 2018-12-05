package com.neaterbits.compiler.codegen.common;

import com.neaterbits.compiler.common.util.Bits;

public class OperandEncoding {
	/*
	 * 
	 * Locals are encoded as
	 * 
	 * 1 bit for stack-based, if not stack based then 4 bits for register
	 * 
	 * |  20 bit type | 4 bits size | 12 bits # of accesses within method | 1 bit loop counter| 1 bit stack| 12 bits register allocation | 
	 * 
	 */

	private static final int OFFSET_BITS = 12;
	private static final long OFFSET_MASK = Bits.mask(OFFSET_BITS);
	
	private static final long IS_STACK = 1L << (OFFSET_BITS);
	private static final long LOOP_COUNTER = 1L << (OFFSET_BITS + 1);
	
	private static final int FLAG_BITS = 2;
	
	
	private static final int ACCESS_COUNT_SHIFT = OFFSET_BITS + FLAG_BITS;
	private static final int ACCESS_COUNT_BITS = 12;
	private static final long ACCESS_COUNT_MASK = Bits.mask(ACCESS_COUNT_BITS, ACCESS_COUNT_SHIFT);
	
	private static final int SIZE_SHIFT = ACCESS_COUNT_SHIFT + ACCESS_COUNT_BITS;
	private static final int SIZE_BITS = 2;
	private static final long SIZE_MASK = Bits.mask(SIZE_BITS, SIZE_SHIFT);
	
	private static final int TYPE_SHIFT = SIZE_SHIFT + SIZE_BITS;
	private static final int TYPE_BITS = 4;
	private static final long TYPE_MASK = Bits.mask(TYPE_BITS, TYPE_SHIFT);

	public static boolean isStackOperand(long encoded) {
		return (encoded & IS_STACK) != 0L;
	}

	public static boolean isRegisterOperand(long encoded) {
		return !isStackOperand(encoded);
	}
	
	public static int getAccessCount(long encoded) {
		return (int)((encoded & ACCESS_COUNT_MASK) >>> ACCESS_COUNT_SHIFT);
	}
	
	public static int getType(long encoded) {
		return (int)((encoded & TYPE_MASK) >>> TYPE_SHIFT);
	}

	public static final int getSize(long encoded) {
		return (int)((encoded & SIZE_MASK) >>> SIZE_SHIFT);
	}
	
	public static int getOffset(long encoded) {
		return (int)(encoded & OFFSET_MASK);
	}
	
	public static long encodeOperand(int type, int accessCount, boolean loopCounter, boolean isStack, int offset) {
		long encoded =
				  ((long)type) << TYPE_SHIFT
				| ((long)accessCount) << TYPE_SHIFT
				| offset;
		
		if (loopCounter) {
			encoded |= LOOP_COUNTER;
		}
		
		if (isStack) {
			encoded |= IS_STACK;
		}
		
		return encoded;
	}
}
