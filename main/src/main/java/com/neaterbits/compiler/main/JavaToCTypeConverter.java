package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.PointerType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.EnumType;
import com.neaterbits.compiler.common.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;
import com.neaterbits.compiler.common.convert.ootofunction.BaseTypeConverter;

final class JavaToCTypeConverter extends BaseTypeConverter{

	@Override
	public TypeReference onString(StringType type, Context param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeReference onPointer(PointerType type, Context param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeReference onClass(ClassType type, Context param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeReference onInterface(InterfaceType type, Context param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeReference onEnum(EnumType type, Context param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeReference onStruct(StructType type, Context param) {
		throw new UnsupportedOperationException();
	}
}
