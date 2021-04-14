package dev.nimbler.compiler.main;

import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.compiler.ast.objects.type.PointerType;
import dev.nimbler.compiler.ast.objects.type.TypeDefType;
import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.type.complex.EnumType;
import dev.nimbler.compiler.ast.objects.type.complex.InterfaceType;
import dev.nimbler.compiler.ast.objects.type.complex.StructType;

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
