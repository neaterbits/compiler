package com.neaterbits.exe.codegen.x86;

public class X86OpCodes {

	public static final byte ADD = 0x03;
	
	public static final byte ADD_IMM8_TO_REG8 = (byte)0x81;
	public static final byte ADD_IMM8_TO_REG_16_32_64 = (byte)0x83;
	
	public static final byte LEA = (byte)8D;
	
	public static final byte MOV_IMM_TO_R = (byte)0xB8;
	
	public static final byte RET_NEAR = (byte)0xC3;
	public static final byte RET_FAR = (byte)0xCB;
	
	public static final byte INC8 = (byte)0xFE;
	public static final byte INC_16_32_64 = (byte)0xFF;

	public static final byte DEC8 = (byte)0xFE;
	public static final byte DEC_16_32_64 = (byte)0xFF;

	public static final byte CALL_ABSOLUTE = (byte)0xFF;
}
