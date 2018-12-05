
#include <stdio.h>
#include <dlfcn.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/mman.h>

#include "com_neaterbits_runtime__native_NativeMethods.h"


JNIEXPORT jint JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_getReferenceSizeInBytes
  (JNIEnv * env, jclass cl) {
	return 8;
}

JNIEXPORT jlong JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_alloc
  (JNIEnv *env, jclass cl, jint size) {

	return (long)malloc(size);
}

JNIEXPORT jlong JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_allocExecutablePages
  (JNIEnv *env, jclass cl, jint size) {

	const int pageSize = sysconf(_SC_PAGE_SIZE);

	if (pageSize == -1) {
		return -1;
	}

	void *mem;

	if (posix_memalign(&mem, pageSize, pageSize * size) < 0) {
		return -1;
	}

	if (mprotect(mem, pageSize * size, PROT_READ|PROT_WRITE|PROT_EXEC) < 0) {
		return -1;
	}


	return (long)mem;
}


JNIEXPORT jlong JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_realloc
  (JNIEnv *env, jclass cl, jlong address, jint size, jint newSize) {

	void *ptr = (void *)address;

	void *newPtr = realloc(ptr, newSize);

	return (long)newPtr;
}

JNIEXPORT void JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_free
  (JNIEnv *env, jclass cl, jlong address) {

	free((void *)address);

}

JNIEXPORT void JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_setReference
  (JNIEnv *env, jclass cl, jlong address, jint offset, jlong reference) {

	void **mem = (void **)address;

	mem[offset] = (void *)reference;

}

JNIEXPORT jlong JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_getReference
  (JNIEnv *env, jclass cl, jlong address, jint offset) {

	void **mem = (void **)address;

	return (long)mem[offset];
}

JNIEXPORT void JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_putString
  (JNIEnv *env, jclass cl, jlong address, jint offset, jstring string) {

	const char *nativeString = (*env)->GetStringUTFChars(env, string, 0);

	char *ptr = (char *)address;

	strcpy(ptr, nativeString);

	(*env)->ReleaseStringUTFChars(env, string, nativeString);

}

JNIEXPORT void JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_putBytes
  (JNIEnv *env, jclass cl, jlong dstAddress, jint dstOffset, jbyteArray srcArray, jint srcOffset, jint length) {

	jboolean isCopy;

	const jbyte *array = (*env)->GetByteArrayElements(env, srcArray, &isCopy);

	char *dst = (char *)dstAddress;

	memcpy(&dst[dstOffset], &array[srcOffset], length);
}


JNIEXPORT void JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_runCode
  (JNIEnv *env, jclass cl, jlong address) {

	void *ptr = (void *)address;

	//goto *ptr;

	/*
	void (*func)() = ptr;

	func();
	*/

	asm("call *%0" : :"r"(ptr));
}

JNIEXPORT jlong JNICALL Java_com_neaterbits_runtime__1native_NativeMethods_getFunctionAddress
  (JNIEnv *env, jclass cl, jstring library, jstring function) {

	const char *nativeLibraryString = (*env)->GetStringUTFChars(env, library, 0);

	void *handle = dlopen(nativeLibraryString, RTLD_LAZY);

	(*env)->ReleaseStringUTFChars(env, library, nativeLibraryString);

	if (handle == NULL) {
		return -1;
	}

	const char *nativeFunctionString = (*env)->GetStringUTFChars(env, function, 0);

	void *address = dlsym(handle, nativeFunctionString);

	(*env)->ReleaseStringUTFChars(env, library, nativeFunctionString);

	if (address == NULL) {
		return -1;
	}

	dlclose(handle);

	return (long)address;
}
