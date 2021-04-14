package dev.nimbler.exe.codegen.common;

public interface CPUArchitecture {

	int getMaxGeneralPurposeRegisterOffset();
	
	int getNumberOfUsableGeneralPurposeRegisters();
	
	boolean isRegisterAddressableForSize(int offset, int size);
	
	int getNumberOfRegistersForOperation(Operation operation);
	
	boolean isMemoryAddressable(int offset);
	
}
