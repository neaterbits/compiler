package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.FunctionPointerType;
import com.neaterbits.compiler.common.ast.type.PointerType;
import com.neaterbits.compiler.common.ast.type.TypeDefType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.EnumType;
import com.neaterbits.compiler.common.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;

public class JavaToCTypeConverterStruct extends BaseJavaToCTypeConverter<JavaToCClassToStructState> {

	@Override
	public BaseType onPointer(PointerType type, JavaToCClassToStructState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onTypeDef(TypeDefType type, JavaToCClassToStructState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onClass(ClassType type, JavaToCClassToStructState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onInterface(InterfaceType type, JavaToCClassToStructState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onEnum(EnumType type, JavaToCClassToStructState param) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public BaseType onStruct(StructType type, JavaToCClassToStructState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseType onFunctionPointer(FunctionPointerType type, JavaToCClassToStructState param) {
		throw new UnsupportedOperationException();
	}
}
