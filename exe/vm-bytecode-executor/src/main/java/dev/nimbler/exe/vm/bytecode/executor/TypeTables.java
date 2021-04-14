package dev.nimbler.exe.vm.bytecode.executor;

import dev.nimbler.runtime._native.NativeMemory;
import dev.nimbler.runtime._native.NativeMethodsTyped;

final class TypeTables {

	private static final int TYPETABLE_INITIAL = 10000;
	
	private final int referenceSizeInBytes;

	private int typeTableSize;
	
	private NativeMemory vtableByType;
	private NativeMemory classObjectByType;
	private NativeMemory staticFieldsByType;

	TypeTables(int referenceSizeInBytes) {
		
		this.referenceSizeInBytes = referenceSizeInBytes;
		
		this.typeTableSize = 0;
	}
	
	void setTablesForType(int type, NativeMemory classVTable, NativeMemory classObjects, NativeMemory staticFields) {
		assureTypeTables(type);

		NativeMethodsTyped.set(vtableByType, type, classVTable);
		NativeMethodsTyped.set(classObjectByType, type, classObjects);
		NativeMethodsTyped.set(staticFieldsByType, type, staticFields);
	}
	
	void setVTableMethod(int type, int vtableIndex, NativeMemory method) {
		final NativeMemory vtable = NativeMethodsTyped.getReference(vtableByType, type);
		
		NativeMethodsTyped.set(vtable, vtableIndex, method);
	}
	
	private void assureTypeTables(int type) {
		
		if (typeTableSize == 0) {
			typeTableSize = type > TYPETABLE_INITIAL ? type * 3 : TYPETABLE_INITIAL;
		
			vtableByType = NativeMethodsTyped.alloc(typeTableSize * referenceSizeInBytes);
			classObjectByType = NativeMethodsTyped.alloc(typeTableSize * referenceSizeInBytes);
			staticFieldsByType = NativeMethodsTyped.alloc(typeTableSize * referenceSizeInBytes);
		}
		else if (type >= typeTableSize) {
			final int newTypeTableSize = type * 3;

			vtableByType = NativeMethodsTyped.realloc(vtableByType, typeTableSize * referenceSizeInBytes, newTypeTableSize * referenceSizeInBytes);
			classObjectByType = NativeMethodsTyped.realloc(classObjectByType, typeTableSize * referenceSizeInBytes, newTypeTableSize * referenceSizeInBytes);
			staticFieldsByType = NativeMethodsTyped.realloc(staticFieldsByType, typeTableSize * referenceSizeInBytes, newTypeTableSize * referenceSizeInBytes);
		}
	}

	
}
