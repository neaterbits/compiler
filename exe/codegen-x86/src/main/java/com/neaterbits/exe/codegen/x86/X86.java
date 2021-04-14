package com.neaterbits.exe.codegen.x86;

public class X86 {

	
	public static int encode32(byte [] dst, int index, byte opcode, byte modRM) {

		dst[index + 0] = opcode;
		dst[index + 1] = modRM;

		return 2;
	}

	
	private static int encode32ByteOperand(byte [] dst, int index, byte opcode, byte modRM, byte operand) {

		dst[index + 0] = opcode;
		dst[index + 1] = modRM;
		dst[index + 2] = operand;

		return 3;
	}

	
	public static int encode32ByteImmediate(byte [] dst, int index, byte opcode, byte modRM, byte immediate) {
		return encode32ByteOperand(dst, index, opcode, modRM, immediate);
	}

	
	public static int encode32ByteDisplacement(byte [] dst, int index, byte opcode, byte modRM, byte displacement) {
		return encode32ByteOperand(dst, index, opcode, modRM, displacement);
	}
	
	private static int encode32ByteOperandWithPrefix(byte [] dst, int index, byte prefix, byte opcode, byte modRM, byte operand) {

		dst[index + 0] = prefix;
		dst[index + 1] = opcode;
		dst[index + 2] = modRM;
		dst[index + 3] = operand;

		return 4;
	}

	public static int encode32ByteImmediateWithPrefix(byte [] dst, int index, byte prefix, byte opcode, byte modRM, byte immediate) {
		return encode32ByteOperandWithPrefix(dst, index, prefix, opcode, modRM, immediate);
	}

	private static int encode32WordOperandWithPrefix(byte [] dst, int index, byte prefix, byte opcode, byte modRM, short operand) {

		dst[index + 0] = prefix;
		dst[index + 1] = opcode;
		dst[index + 2] = modRM;
		dst[index + 3] = (byte)(operand & 0xFF);
		dst[index + 4] = (byte)(operand >>> 8);

		return 5;
	}

	public static int encode32SWordImmediateWithPrefix(byte [] dst, int index, byte prefix, byte opcode, byte modRM, short immediate) {
		return encode32WordOperandWithPrefix(dst, index, prefix, opcode, modRM, immediate);
	}

	private static int encode32SDWordOperand(byte [] dst, int index, byte opcode, byte modRM, int operand) {

		dst[index + 0] = opcode;
		dst[index + 1] = modRM;
		dst[index + 2] = (byte)(operand & 0x000000FF);
		dst[index + 3] = (byte)(operand >> 8 & 0x000000FF);
		dst[index + 4] = (byte)(operand >> 16 & 0x000000FF);
		dst[index + 5] = (byte)(operand >> 24 & 0x000000FF);

		return 6;
	}

	public static int encode32SDWordDisplacement(byte [] dst, int index, byte opcode, byte modRM, int displacement) {
		return encode32SDWordOperand(dst, index, opcode, modRM, displacement);
	}

	public static int encode32SDWordImmediate(byte [] dst, int index, byte opcode, byte modRM, int immediate) {
		return encode32SDWordOperand(dst, index, opcode, modRM, immediate);
	}

	
	public static int encode(byte [] dst, int index, byte opcode, byte modRM) {
		dst[index + 0] = opcode;
		dst[index + 1] = modRM;
		
		return 2;
	}

	public static int encodeWithPrefix(byte [] dst, int index, byte prefix, byte opcode, byte modRM) {
		dst[index + 0] = prefix;
		dst[index + 1] = opcode;
		dst[index + 2] = modRM;
		
		return 3;
	}

	public static int encodeREX(byte [] dst, int index, byte rex, byte opcode, byte modRM) {
		
		dst[index + 0] = rex;
		dst[index + 1] = opcode;
		dst[index + 2] = modRM;
		
		return 3;
	}

	
	private static int encodeREXByteOperand(byte [] dst, int index, byte rex, byte opcode, byte modRM, byte operand) {
		
		dst[index + 0] = rex;
		dst[index + 1] = opcode;
		dst[index + 2] = modRM;
		dst[index + 3] = operand;
		
		return 4;
	}

	public static int encodeREXByteImmediate(byte [] dst, int index, byte rex, byte opcode, byte modRM, byte immediate) {
		return encodeREXByteOperand(dst, index, rex, opcode, modRM, immediate);
	}

	
	public static int encodeREXByteDisplacement(byte [] dst, int index, byte rex, byte opcode, byte modRM, byte displacement) {
		return encodeREXByteOperand(dst, index, rex, opcode, modRM, displacement);
	}

	public static int encodeREXIntDisplacement(byte [] dst, int index, byte rex, byte opcode, byte modRM, int displacement) {
		
		dst[index + 0] = rex;
		dst[index + 1] = opcode;
		dst[index + 2] = modRM;
		dst[index + 3] = (byte)(displacement & 0x000000FF);
		dst[index + 4] = (byte)(displacement >> 8 & 0x000000FF);
		dst[index + 5] = (byte)(displacement >> 16 & 0x000000FF);
		dst[index + 6] = (byte)(displacement >> 24 & 0x000000FF);
		
		return 7;
	}

	private static int encodeREXSQWord(byte [] dst, int index, byte rex, byte opcode, long longValue) {
		dst[index + 0] = rex;
		dst[index + 1] = opcode;
		dst[index + 2] = (byte)(longValue >> 0  & 0x00000000000000FFL);
		dst[index + 3] = (byte)(longValue >> 8  & 0x00000000000000FFL);
		dst[index + 4] = (byte)(longValue >> 16 & 0x00000000000000FFL);
		dst[index + 5] = (byte)(longValue >> 24 & 0x00000000000000FFL);
		dst[index + 6] = (byte)(longValue >> 32 & 0x00000000000000FFL);
		dst[index + 7] = (byte)(longValue >> 40 & 0x00000000000000FFL);
		dst[index + 8] = (byte)(longValue >> 48 & 0x00000000000000FFL);
		dst[index + 9] = (byte)(longValue >> 56 & 0x00000000000000FFL);
		
		return 10;
	}

	
	private static int encodeREXSQWord(byte [] dst, int index, byte rex, byte opcode, byte modRM, long longValue) {
		
		dst[index + 0]  = rex;
		dst[index + 1]  = opcode;
		dst[index + 2]  = modRM;
		dst[index + 3]  = (byte)(longValue >> 0  & 0x00000000000000FFL);
		dst[index + 4]  = (byte)(longValue >> 8  & 0x00000000000000FFL);
		dst[index + 5]  = (byte)(longValue >> 16 & 0x00000000000000FFL);
		dst[index + 6]  = (byte)(longValue >> 24 & 0x00000000000000FFL);
		dst[index + 7]  = (byte)(longValue >> 32 & 0x00000000000000FFL);
		dst[index + 8]  = (byte)(longValue >> 40 & 0x00000000000000FFL);
		dst[index + 9]  = (byte)(longValue >> 48 & 0x00000000000000FFL);
		dst[index + 10] = (byte)(longValue >> 56 & 0x00000000000000FFL);
		
		return 11;
	}

	public static int encodeREXSQWordDisplacement(byte [] dst, int index, byte rex, byte opcode, byte modRM, long displacement) {
		return encodeREXSQWord(dst, index, rex, opcode, modRM, displacement);
	}
	
	public static int encodeREXSQWordImmediate(byte [] dst, int index, byte rex, byte opcode, long immediate) {
		return encodeREXSQWord(dst, index, rex, opcode, immediate);
	}
	
	public static int encodeREXSQWordImmediate(byte [] dst, int index, byte rex, byte opcode, byte modRM, long immediate) {
		return encodeREXSQWord(dst, index, rex, opcode, modRM, immediate);
	}
}
