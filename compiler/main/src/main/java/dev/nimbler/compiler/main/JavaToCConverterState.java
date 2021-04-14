package dev.nimbler.compiler.main;

import java.util.Objects;

import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.type.complex.StructType;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;

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
