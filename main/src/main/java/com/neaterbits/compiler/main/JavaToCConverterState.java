package com.neaterbits.compiler.main;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;

final class JavaToCConverterState extends MappingJavaToCConverterState<JavaToCConverterState> {
	
	private final Map<ComplexType, StructType> complexToStruct;
	
	public JavaToCConverterState(Map<ComplexType, StructType> complexToStruct) {
		super(new JavaToCConverters()
		);

		Objects.requireNonNull(complexToStruct);
	
		this.complexToStruct = complexToStruct;
	}

	StructType getStructTypeForClass(ClassType classType) {
		Objects.requireNonNull(classType);
		
		return complexToStruct.get(classType);
	}
}
