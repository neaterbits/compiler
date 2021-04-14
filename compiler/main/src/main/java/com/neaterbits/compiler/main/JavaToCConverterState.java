package com.neaterbits.compiler.main;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.type.complex.ClassType;
import com.neaterbits.compiler.ast.objects.type.complex.StructType;
import com.neaterbits.language.codemap.compiler.CompilerCodeMap;

final class JavaToCConverterState extends MappingJavaToCConverterState<JavaToCConverterState> {

	private final JavaToCDeclarations declarations;

	public JavaToCConverterState(JavaToCDeclarations declarations, CompilerCodeMap codeMap) {

		super(new JavaToCConverters(), codeMap);

		Objects.requireNonNull(declarations);

		this.declarations = declarations;
	}

	StructType getStructTypeForClass(ClassType classType) {
		Objects.requireNonNull(classType);

		return declarations.getClassStructType(classType.getTypeName());
	}
}
