package com.neaterbits.compiler.main;

import java.util.Objects;

import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.StructType;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMap;

final class JavaToCConverterState extends MappingJavaToCConverterState<JavaToCConverterState> {
	
	private final JavaToCDeclarations declarations;
	
	public JavaToCConverterState(JavaToCDeclarations declarations, ResolvedTypeCodeMap codeMap) {
		super(new JavaToCConverters(), codeMap);

		Objects.requireNonNull(declarations);
	
		this.declarations = declarations;
	}

	StructType getStructTypeForClass(ClassType classType) {
		Objects.requireNonNull(classType);
		
		return declarations.getClassStructType(classType);
	}
}
