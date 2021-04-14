package dev.nimbler.compiler.main;

import dev.nimbler.compiler.convert.Converters;

public final class JavaToCConverters extends Converters<JavaToCConverterState> {

	public JavaToCConverters() {
		super(new JavaToCStatementConverter<>(),
				new JavaToCExpressionConverter<>(),
				new JavaToCVariableReferenceConverter<>(),
				new JavaToCTypeReferenceConverter<>(),
				new JavaToCTypeConverter());
	}
	
}
