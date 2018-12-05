package com.neaterbits.runtime._native;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class RuntimeNativeTest {

	@Before
	public void loadLibrary() {

		NativeMethodsTyped.load();
	
	}
	
	@Test
	public void testGetReferenceSize() {
		
		assertThat(NativeMethods.getReferenceSizeInBytes()).isEqualTo(8);

	}
		
	@Test
	public void testGetSymbol() {

		final long address = NativeMethods.getFunctionAddress("/lib/x86_64-linux-gnu/libc.so.6", "open");
		
		System.out.println("## got address " + address);
		
		assertThat(address).isNotEqualTo(-1);
	}

	@Test
	public void testAlloc() {
		
		final long address = NativeMethods.alloc(125);
		
		assertThat(address).isGreaterThan(0L);
		
		NativeMethods.free(address);
	}

	@Test
	public void testAllocExecutablePages() {
		final long address = NativeMethods.allocExecutablePages(1);
		
		assertThat(address).isGreaterThan(0L);
	}

	@Test
	public void testReAlloc() {
		
		final long address = NativeMethods.alloc(125);
		
		assertThat(address).isGreaterThan(0L);
		
		NativeMethods.setReference(address, 16, 0x12345);
		
		final long realloced = NativeMethods.realloc(address, 125, 234);
		
		assertThat(realloced).isNotEqualTo(address);
		
		assertThat(NativeMethods.getReference(realloced, 16)).isEqualTo(0x12345);
		
		NativeMethods.free(realloced);
	}
}
