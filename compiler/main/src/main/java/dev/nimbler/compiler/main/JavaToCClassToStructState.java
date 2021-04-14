package dev.nimbler.compiler.main;

import dev.nimbler.compiler.convert.Converters;

public class JavaToCClassToStructState extends MappingJavaToCConverterState<JavaToCClassToStructState> {

	public JavaToCClassToStructState() {
		super(new Converters<JavaToCClassToStructState>(
				new JavaToCStatementConverter<>(),
				new JavaToCExpressionConverter<>(),
				new JavaToCVariableReferenceConverter<>(),
				new JavaToCTypeReferenceConverter<>(),
				new JavaToCTypeConverterStruct()),
				null);
	}
}
