package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.PointerType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.EnumType;
import com.neaterbits.compiler.common.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;
import com.neaterbits.compiler.common.convert.ootofunction.BaseTypeConverter;

final class JavaToCTypeConverter extends BaseTypeConverter<JavaToCConverterState> {

	@Override
	public BaseType onString(StringType type, JavaToCConverterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onPointer(PointerType type, JavaToCConverterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onClass(ClassType type, JavaToCConverterState param) {

		StructType structType = param.getStructTypeForClass(type);
		
		if (structType == null) {
			throw new IllegalStateException("No struct type for " + type.getName());
		}

		return new PointerType(structType, 1);
	}

	@Override
	public BaseType onInterface(InterfaceType type, JavaToCConverterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onEnum(EnumType type, JavaToCConverterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onStruct(StructType type, JavaToCConverterState param) {
		throw new UnsupportedOperationException();
	}
}
