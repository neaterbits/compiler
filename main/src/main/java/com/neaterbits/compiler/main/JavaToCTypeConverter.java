package com.neaterbits.compiler.main;

import com.neaterbits.compiler.ast.objects.type.BaseType;
import com.neaterbits.compiler.ast.objects.type.FunctionPointerType;
import com.neaterbits.compiler.ast.objects.type.PointerType;
import com.neaterbits.compiler.ast.objects.type.TypeDefType;
import com.neaterbits.compiler.ast.objects.type.complex.ClassType;
import com.neaterbits.compiler.ast.objects.type.complex.EnumType;
import com.neaterbits.compiler.ast.objects.type.complex.InterfaceType;
import com.neaterbits.compiler.ast.objects.type.complex.StructType;

final class JavaToCTypeConverter extends BaseJavaToCTypeConverter<JavaToCConverterState> {

	@Override
	public BaseType onPointer(PointerType type, JavaToCConverterState param) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public BaseType onTypeDef(TypeDefType type, JavaToCConverterState param) {
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

	@Override
	public BaseType onFunctionPointer(FunctionPointerType type, JavaToCConverterState param) {
		throw new UnsupportedOperationException();
	}
}
