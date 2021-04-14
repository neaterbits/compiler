package dev.nimbler.compiler.main;

import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.compiler.ast.objects.type.PointerType;
import dev.nimbler.compiler.ast.objects.type.TypeDefType;
import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.type.complex.EnumType;
import dev.nimbler.compiler.ast.objects.type.complex.InterfaceType;
import dev.nimbler.compiler.ast.objects.type.complex.StructType;

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
