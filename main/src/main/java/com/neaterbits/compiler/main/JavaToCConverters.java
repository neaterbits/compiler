package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.convert.Converters;

public final class JavaToCConverters extends Converters<JavaToCConverterState> {

	public JavaToCConverters() {
		super(new JavaToCStatementConverter<>(),
				new JavaToCExpressionConverter<>(),
				new JavaToCVariableReferenceConverter<>(),
				new JavaToCTypeReferenceConverter<>(),
				new JavaToCTypeConverter());
	}
	
}
