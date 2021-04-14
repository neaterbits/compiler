package dev.nimbler.runtime._native;

class NativeMethods {

	static native int getReferenceSizeInBytes();
	
	static native long alloc(int size);

	static native long allocExecutablePages(int size);

	static native long realloc(long memory, int size, int newSize);

	static native void free(long memory);
	
	static native void clear(long memory, int size);
	
	static native void setReference(long memory, int offset, long reference);
	
	static native long getReference(long memory, int offset);

	static native void putString(long memory, int offset, String string);

	static native void putBytes(long memory, int dstOffset, byte [] src, int srcOffset, int length);
	
	static native void runCode(long memory);
	
	static native long getMemoryAddress(Object object);
	
	// Library function address
	static native long getFunctionAddress(String library, String name);
}
