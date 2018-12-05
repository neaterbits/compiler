package com.neaterbits.compiler.codegen.x86;

public class MOD {

	private static final byte MOD_SHIFT = 6;
	
	public static final byte REGISTER_INDIRECT = 0;
	public static final byte REGISTER_INDIRECT_DISP8 = (byte)(1 << MOD_SHIFT);
	public static final byte REGISTER_INDIRECT_DISP32 = (byte)(2 << MOD_SHIFT);
	public static final byte REGISTER = (byte)(3 << MOD_SHIFT);
	
	
}
