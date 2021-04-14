package dev.nimbler.exe.vm.bytecode.executor;

import java.util.List;
import java.util.Objects;

import dev.nimbler.exe.codegen.common.CodeGenerator;
import dev.nimbler.exe.vm.bytecode.loader.BytecodeCompiler;
import dev.nimbler.exe.vm.bytecode.loader.BytecodeLoader;
import dev.nimbler.exe.vm.bytecode.loader.ThreadData;
import dev.nimbler.exe.vm.bytecode.loader.codemap.IntLoaderCodeMap;
import dev.nimbler.exe.vm.bytecode.loader.codemap.LoaderCodeMap;
import dev.nimbler.language.bytecode.common.BytecodeFormat;
import dev.nimbler.language.codemap.DynamicMethodOverrideMap;
import dev.nimbler.language.common.types.FieldType;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypeVariant;
import dev.nimbler.language.common.typesources.libs.ClassLibs;

public class ExecutorMain<CLASS, METHOD> {
	
	private final String mainMethodName;
	private final List<FieldType> mainParameterTypes;
	private final LanguageClassTypes languageClassTypes;

	ExecutorMain(String mainMethodName, List<FieldType> mainParameterTypes, LanguageClassTypes languageClassTypes) {
		
		Objects.requireNonNull(mainMethodName);
		Objects.requireNonNull(mainParameterTypes);
		Objects.requireNonNull(languageClassTypes);
		
		this.mainMethodName = mainMethodName;
		this.mainParameterTypes = mainParameterTypes;
		this.languageClassTypes = languageClassTypes;
	}

	public void execute(TypeName className, BytecodeFormat bytecodeFormat, ClassLibs classLibs, CodeGenerator codeGenerator) {

		final LoaderCodeMap codeMap = new SynchronizedLoaderCodeMap(new IntLoaderCodeMap(new DynamicMethodOverrideMap()));
		
		final int baseType = codeMap.addType(TypeVariant.CLASS, new int[0], null);
		final int classType = codeMap.addType(TypeVariant.CLASS, new int [] { baseType }, null);

		final BytecodeCompiler<CompiledClass, Void> compiler = new ExecutorBytecodeCompiler(
				baseType,
				classType,
				languageClassTypes,
				codeMap);
		
		
		final BytecodeLoader<CompiledClass, Void> bytecodeLoader = new BytecodeLoader<>(
				bytecodeFormat,
				classLibs,
				compiler,
				codeMap);
		
		final ThreadData mainThread = new ThreadData() {
			@Override
			public int getThreadNo() {
				throw new UnsupportedOperationException();
			}
		};
		
		bytecodeLoader.loadInitialMethod(mainThread, className, mainMethodName, mainParameterTypes);
	}
}
