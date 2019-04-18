package com.neaterbits.compiler.bytecode.common.executor;

import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.loader.BytecodeCompiler;
import com.neaterbits.compiler.bytecode.common.loader.codemap.LoaderCodeMap;
import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.codemap.VTableScratchArea;
import com.neaterbits.runtime._native.NativeMemory;
import com.neaterbits.runtime._native.NativeMethodsTyped;

final class ExecutorBytecodeCompiler implements BytecodeCompiler<CompiledClass, Void> {

	private final LoaderCodeMap codeMap;
	
	private final ClassObjectClass classObjectFields;
	
	private final int classObjectMemorySize;
	
	private final int referenceSizeInBytes;
	
	private final TypeTables typeTables;
	
	private int[][] classVTable;
	
	ExecutorBytecodeCompiler(int baseType, int classType, LanguageClassTypes languageClassTypes, LoaderCodeMap codeMap) {
		Objects.requireNonNull(languageClassTypes);
		Objects.requireNonNull(codeMap);
		
		this.codeMap = codeMap;

		this.classObjectFields = new ClassObjectClass(languageClassTypes);
		
		this.referenceSizeInBytes = NativeMethodsTyped.getReferenceSizeInBytes();
		
		this.typeTables = new TypeTables(referenceSizeInBytes);
		
		final long [] sortedClassObjectFields = FieldSorter.sortFieldsForType(
				classObjectFields,
				referenceSizeInBytes,
				fieldIdx -> !classObjectFields.isStatic(fieldIdx));

		this.classObjectMemorySize = FieldSorter.getFieldsMemorySize(sortedClassObjectFields);
		
		final VTableScratchArea scratchArea = new VTableScratchArea();
		
		initializeClass(baseType, new BaseTypeClass(languageClassTypes), scratchArea);
		initializeClass(classType, classObjectFields, scratchArea);
	}
	
	
	@Override
	public CompiledClass initializeClass(int type, ClassBytecode classBytecode, VTableScratchArea scratchArea) {

		final NativeMemory classVTable = allocateVTable(type, scratchArea);
		
		final NativeMemory classObjects = compileClassObject(classBytecode);
		
		final NativeMemory staticFields = compileStaticFields(classBytecode);
		
		typeTables.setTablesForType(type, classVTable, classObjects, staticFields);
		
		return new CompiledClass(classVTable, classObjects, staticFields);
	}
	
	private int getVTableSize(int type, VTableScratchArea scratchArea) {

		final int vtableSize = codeMap.getDistinctMethodCount(type, (methodNo, variant) -> true, scratchArea);
		
		synchronized (this) {
			if (type >= classVTable.length) {
				classVTable = Arrays.copyOf(classVTable, type * 3);
			}
			
			classVTable[type] = scratchArea.copyVTable();
		}
		
		return vtableSize;
	}
	
	private NativeMemory allocateVTable(int type, VTableScratchArea scratchArea) {
		
		final int vtableMethodCount = getVTableSize(type, scratchArea);
		
		final NativeMemory vtable = NativeMethodsTyped.alloc(vtableMethodCount * referenceSizeInBytes);
		
		return vtable;
	}

	private NativeMemory compileClassObject(ClassBytecode classBytecode) {
		
		final NativeMemory classObject = NativeMethodsTyped.alloc(classObjectMemorySize);

		return classObject;
	}
	
	private NativeMemory compileStaticFields(ClassBytecode classBytecode) {

		final long [] fields = FieldSorter.sortFieldsForType(classBytecode, referenceSizeInBytes, fieldIdx -> classBytecode.isStatic(fieldIdx));
		
		final int size = FieldSorter.getFieldsMemorySize(fields);
		
		final NativeMemory memory = NativeMethodsTyped.alloc(size);
		
		NativeMethodsTyped.clear(memory, size);
		
		return memory;
	}
	
	@Override
	public Void compileMethod(int type, int methodIdx, MethodVariant methodVariant, byte [] bytecode) {

		switch (methodVariant) {
		
		case OVERRIDABLE_IMPLEMENTATION:
		case FINAL_IMPLEMENTATION:
			break;
			
		default:
			throw new UnsupportedOperationException();
		}

		final int vtableIndex = classVTable[type][methodIdx];
		
		typeTables.setVTableMethod(type, vtableIndex, null);
		
		return null;
	}
}
