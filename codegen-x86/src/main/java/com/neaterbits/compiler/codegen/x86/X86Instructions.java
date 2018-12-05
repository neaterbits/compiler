package com.neaterbits.compiler.codegen.x86;

public class X86Instructions extends X86 {

	public static int MOVABS_REX(byte [] buffer, int index, byte rex, byte register, long immediate) {

		return encodeREXSQWordImmediate(buffer, index, rex, (byte)(X86OpCodes.MOV_IMM_TO_R | register), immediate);
		
	}

	public static int RET_NEAR(byte [] buffer, int index) {
		
		buffer[index] = X86OpCodes.RET_NEAR;
		
		return 1;
	}

	public static int RET_FAR(byte [] buffer, int index) {
		
		buffer[index] = X86OpCodes.RET_FAR;
		
		return 1;
	}
	
	public static final int CALL_INDIRECT(byte [] buffer, int index, byte register) {
		
		final byte modRM = (byte)(MOD.REGISTER | 2 << 3 | register);
		
		
		System.out.format("modRM: %02x\n", modRM);
		
		return encode(buffer, index, X86OpCodes.CALL_ABSOLUTE, modRM);
	}
	
	public static final int INC8(byte [] buffer, int index, byte register) {
		return encode(buffer, index, X86OpCodes.INC8, (byte)(MOD.REGISTER|register));
	}

	public static final int INC16(byte [] buffer, int index, byte register) {
		return encodeWithPrefix(buffer, index, X86Prefixes.WORD_OPERAND, X86OpCodes.INC_16_32_64, (byte)(MOD.REGISTER|register));
	}

	public static final int INC32(byte [] buffer, int index, byte register) {
		return encode(buffer, index, X86OpCodes.INC_16_32_64, (byte)(MOD.REGISTER|register));
	}

	public static final int INC64(byte [] buffer, int index, byte rex, byte register) {
		return encodeREX(buffer, index, rex, X86OpCodes.INC_16_32_64, (byte)(MOD.REGISTER|register));
	}

	public static final int DEC8(byte [] buffer, int index, byte register) {
		return encode(buffer, index, X86OpCodes.DEC8, (byte)(MOD.REGISTER | 1 << 3 | register));
	}

	public static final int DEC16(byte [] buffer, int index, byte register) {
		return encodeWithPrefix(buffer, index, X86Prefixes.WORD_OPERAND, X86OpCodes.DEC_16_32_64, (byte)(MOD.REGISTER | 1 << 3 | register));
	}

	public static final int DEC32(byte [] buffer, int index, byte register) {
		return encode(buffer, index, X86OpCodes.DEC_16_32_64, (byte)(MOD.REGISTER | 1 << 3 | register));
	}

	public static final int DEC64(byte [] buffer, int index, byte rex, byte register) {
		return encodeREX(buffer, index, rex, X86OpCodes.DEC_16_32_64, (byte)(MOD.REGISTER | 1 << 3 | register));
	}

	public static final int ADD_SIGNED_IMM8_TO_REG8(byte [] buffer, int index, byte register, byte immediate) {
		return encode32ByteImmediate(buffer, index, X86OpCodes.ADD_IMM8_TO_REG8, (byte)(MOD.REGISTER|register), immediate);
	}

	public static final int ADD_SIGNED_IMM8_TO_REG16(byte [] buffer, int index, byte register, byte immediate) {
		return encode32ByteImmediateWithPrefix(buffer, index, X86Prefixes.WORD_OPERAND, X86OpCodes.ADD_IMM8_TO_REG_16_32_64, (byte)(MOD.REGISTER|register), immediate);
	}

	public static final int ADD_SIGNED_IMM8_TO_REG32(byte [] buffer, int index, byte register, byte immediate) {
		return encode32ByteImmediate(buffer, index, X86OpCodes.INC_16_32_64, (byte)(MOD.REGISTER|register), immediate);
	}

	public static final int ADD_SIGNED_IMM8_TO_REG64(byte [] buffer, int index, byte rex, byte register, byte immediate) {
		return encodeREXByteImmediate(buffer, index, rex, X86OpCodes.INC_16_32_64, (byte)(MOD.REGISTER|register), immediate);
	}
	
	public static final int LEA64_16BIT_OFFSET(byte [] buffer, int index, byte register, short offset) {
		return encode32SWordImmediateWithPrefix(
				buffer,
				index,
				X86Prefixes.WORD_OPERAND,
				X86OpCodes.LEA,
				(byte)(MOD.REGISTER_INDIRECT_DISP32 | register),
				offset);
	}

	public static final int LEA64_32BIT_OFFSET(byte [] buffer, int index, byte register, int offset) {
		return encode32SDWordImmediate(
				buffer,
				index,
				X86OpCodes.LEA,
				(byte)(MOD.REGISTER_INDIRECT_DISP32 | register),
				offset);
	}
	
	public static final int LEA64_64BIT_OFFSET(byte [] buffer, int index, byte rex, byte register, long offset) {
		
		if (offset < 0) {
			throw new IllegalArgumentException();
		}
		
		return encodeREXSQWordImmediate(
				buffer,
				index,
				rex,
				X86OpCodes.LEA,
				(byte)(MOD.REGISTER_INDIRECT_DISP32 | register),
				offset);
	}
}
