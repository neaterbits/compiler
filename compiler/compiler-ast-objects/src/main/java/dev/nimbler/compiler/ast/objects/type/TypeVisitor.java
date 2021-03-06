package dev.nimbler.compiler.ast.objects.type;

import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.type.complex.EnumType;
import dev.nimbler.compiler.ast.objects.type.complex.InterfaceType;
import dev.nimbler.compiler.ast.objects.type.complex.StructType;
import dev.nimbler.compiler.ast.objects.type.primitive.ArrayType;
import dev.nimbler.compiler.ast.objects.type.primitive.BooleanType;
import dev.nimbler.compiler.ast.objects.type.primitive.ByteType;
import dev.nimbler.compiler.ast.objects.type.primitive.Char16Type;
import dev.nimbler.compiler.ast.objects.type.primitive.Char8Type;
import dev.nimbler.compiler.ast.objects.type.primitive.DoubleType;
import dev.nimbler.compiler.ast.objects.type.primitive.FloatType;
import dev.nimbler.compiler.ast.objects.type.primitive.IntType;
import dev.nimbler.compiler.ast.objects.type.primitive.LongType;
import dev.nimbler.compiler.ast.objects.type.primitive.NamedVoidType;
import dev.nimbler.compiler.ast.objects.type.primitive.NullType;
import dev.nimbler.compiler.ast.objects.type.primitive.ShortType;
import dev.nimbler.compiler.ast.objects.type.primitive.StringType;
import dev.nimbler.compiler.ast.objects.type.primitive.UnnamedVoidType;

public interface TypeVisitor<T, R> {
	
	R onByte(ByteType type, T param);
	
	R onChar8(Char8Type type, T param);
	
	R onChar16(Char16Type type, T param);
	
	R onShort(ShortType type, T param);

	R onInt(IntType type, T param);

	R onLong(LongType type, T param);

	R onFloat(FloatType type, T param);

	R onDouble(DoubleType type, T param);
	
	R onBoolean(BooleanType type, T param);
	
	R onVoid(NamedVoidType type, T param);
	
	R onString(StringType type, T param);
	
	R onPointer(PointerType type, T param);

	R onClass(ClassType type, T param);
	
	R onInterface(InterfaceType type, T param);

	R onEnum(EnumType type, T param);
	
	R onArray(ArrayType type, T param);

	R onStruct(StructType type, T param);

	R onFunctionPointer(FunctionPointerType type, T param);
	
	R onTypeDef(TypeDefType type, T param);

	R onNullType(NullType type, T param);
	
	R onUnnamedVoidType(UnnamedVoidType type, T param);
}
