package com.neaterbits.runtime._native;

public class NativeMethodsTyped {
	
    public static boolean isLinux() {
        
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

	public static void load() {
		System.load(System.getenv("HOME") + "/projects/compiler/runtime-linux/Debug/liblinuxjni.so");
	}
	
	public static int getReferenceSizeInBytes() {
		return NativeMethods.getReferenceSizeInBytes();
	}
	
	public static NativeMemory alloc(int size) {
		return new NativeMemory(NativeMethods.alloc(size));
	}
	
	public static NativeMemory allocExecutablePages(int size) {
		final long mem = NativeMethods.allocExecutablePages(size);
		
		return mem < 0 ? null : new NativeMemory(mem);
	}

	public static NativeMemory realloc(NativeMemory memory, int size, int newSize) {
		return new NativeMemory(NativeMethods.realloc(memory.getAddress(), size, newSize));
	}

	public static void free(NativeMemory memory) {
		NativeMethods.free(memory.getAddress());
	}
	
	public static void clear(NativeMemory memory, int size) {
		NativeMethods.clear(memory.getAddress(), size);
	}
	
	public static void set(NativeMemory memory, int offset, NativeMemory reference) {
		NativeMethods.setReference(memory.getAddress(), offset, reference.getAddress());
	}
	

	public static NativeMemory getReference(NativeMemory memory, int offset) {
		return new NativeMemory(NativeMethods.getReference(memory.getAddress(), offset));
	}
	
	public static void putString(NativeMemory memory, int offset, String string) {
		NativeMethods.putString(memory.getAddress(), offset, string);
	}
	
	public static void putBytes(NativeMemory memory, int dstOffset, byte [] src, int srcOffset, int length) {
		NativeMethods.putBytes(memory.getAddress(), dstOffset, src, srcOffset, length);
	}

	public static void runCode(NativeMemory memory) {
		NativeMethods.runCode(memory.getAddress());
	}
	
	public static long getMemoryAddress(Object object) {
		return NativeMethods.getMemoryAddress(object);
	}
	
	// Library function address
	public static long getFunctionAddress(String library, String name) {
		return NativeMethods.getFunctionAddress(library, name);
	}
}
