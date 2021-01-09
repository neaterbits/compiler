package com.neaterbits.compiler.bytecode.common.executor;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.ClassLibs;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.bytecode.common.loader.BytecodeCompiler;
import com.neaterbits.compiler.bytecode.common.loader.BytecodeLoader;
import com.neaterbits.compiler.bytecode.common.loader.ThreadData;
import com.neaterbits.compiler.bytecode.common.loader.codemap.IntLoaderCodeMap;
import com.neaterbits.compiler.bytecode.common.loader.codemap.LoaderCodeMap;
import com.neaterbits.compiler.codegen.common.CodeGenerator;
import com.neaterbits.compiler.codemap.DynamicMethodOverrideMap;
import com.neaterbits.compiler.types.TypeVariant;
import com.neaterbits.compiler.util.FieldType;

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
