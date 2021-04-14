package dev.nimbler.exe.vm.bytecode.loader;

import java.util.function.BiConsumer;

import dev.nimbler.exe.vm.bytecode.loader.HashTypeMap.CreateType;
import dev.nimbler.exe.vm.bytecode.loader.HashTypeMap.LoadType;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.codemap.CodeMap;

public class LoadClassParameters<CLASSLIBS, TYPE, CACHE> {

	final HashTypeMap<TYPE> typeMap;
	final CodeMap codeMap;
	final CreateType<TYPE> createType;
	final BiConsumer<Integer, ClassBytecode> onAddedType;
	final LoadType loadType;

	public LoadClassParameters(
		HashTypeMap<TYPE> typeMap,
		CodeMap codeMap,
		CreateType<TYPE> createType,
		BiConsumer<Integer, ClassBytecode> onAddedType,
		LoadType loadType) {
	
		this.typeMap = typeMap;
		this.codeMap = codeMap;
		this.createType = createType;
		this.onAddedType = onAddedType;
		this.loadType = loadType;
	}
}
