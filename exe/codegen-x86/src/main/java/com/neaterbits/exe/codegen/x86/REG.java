package com.neaterbits.exe.codegen.x86;


public class REG {

	private static final byte REG_SHIFT = 3;
	
	public static final byte AX = (byte)(X86Registers.AX << REG_SHIFT);
	public static final byte CX = (byte)(X86Registers.CX << REG_SHIFT);
	public static final byte DX = (byte)(X86Registers.DX << REG_SHIFT);
	public static final byte BX = (byte)(X86Registers.BX << REG_SHIFT);
	public static final byte SP = (byte)(X86Registers.SP << REG_SHIFT);
	public static final byte BP = (byte)(X86Registers.BP << REG_SHIFT);
	public static final byte SI = (byte)(X86Registers.SI << REG_SHIFT);
	public static final byte DI = (byte)(X86Registers.DI << REG_SHIFT);
	public static final byte R8 = (byte)(X86Registers.R8 << REG_SHIFT);
	public static final byte R9 = (byte)(X86Registers.R9 << REG_SHIFT);
	public static final byte R10 = (byte)(X86Registers.R10 << REG_SHIFT);
	public static final byte R11 = (byte)(X86Registers.R11 << REG_SHIFT);
	public static final byte R12 = (byte)(X86Registers.R12 << REG_SHIFT);
	public static final byte R13 = (byte)(X86Registers.R13 << REG_SHIFT);
	public static final byte R14 = (byte)(X86Registers.R14 << REG_SHIFT);
	public static final byte R15 = (byte)(X86Registers.R15 << REG_SHIFT);
	
}
