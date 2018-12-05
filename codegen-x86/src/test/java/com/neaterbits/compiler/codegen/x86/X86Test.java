package com.neaterbits.compiler.codegen.x86;

import org.junit.Test;

import com.neaterbits.runtime._native.NativeMemory;
import com.neaterbits.runtime._native.NativeMethodsTyped;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

public class X86Test {

	@Test
	public void testCode() {
		
		NativeMethodsTyped.load();
		
		final NativeMemory nativeMemory = NativeMethodsTyped.allocExecutablePages(1);
		
		final byte [] bytes = new byte[10000];
		
		final NativeMemory stringMemory = NativeMethodsTyped.alloc(15);
		
		NativeMethodsTyped.putString(stringMemory, 0, "abc123\n");
		
		final long putc = NativeMethodsTyped.getFunctionAddress("libc.so.6", "puts");
		
		assertThat(putc).isGreaterThan(0L);
		
		int idx = 0;
		
		System.out.format("## call code at %016x\n", nativeMemory.getAddress());
		
		idx += X86Instructions.MOVABS_REX(bytes, idx, (byte)(REX.ENABLE|REX.W), X86Registers.DI, stringMemory.getAddress());
		
		idx += X86Instructions.MOVABS_REX(bytes, idx, (byte)(REX.ENABLE|REX.W), X86Registers.AX, putc);
		
		idx += X86Instructions.CALL_INDIRECT(bytes, idx, X86Registers.AX);
		
		idx += X86Instructions.RET_NEAR(bytes, idx);
		
		final byte [] instructions = Arrays.copyOf(bytes, idx);
		
		hexdump(instructions);

		NativeMethodsTyped.putBytes(nativeMemory, 0, bytes, 0, idx);
		
		NativeMethodsTyped.runCode(nativeMemory);
	}
	
	private static void hexdump(byte [] buffer) {
		
		System.out.println("hexdump of " + buffer.length);
		
		for (int i = 0; i < buffer.length; ++ i) {
			System.out.format("%02x", buffer);
			
			/*
			hexdump((byte)(b >>> 4));
			hexdump((byte)(b & 0x0F));
			*/
		}
		
		System.out.println();
	}
	
	/*
	private static void hexdump(byte b) {

		if (b < 0) {
			throw new IllegalArgumentException();
		}
		
		if (b > 15) {
			throw new IllegalArgumentException();
		}
		
		final char c;
		
		if (b < 10) {
			c = (char)('0' + b);
		}
		else {
			c = (char)('A' + (b - 10));
		}
		
		System.out.print(c);
	}
	*/
	
}
