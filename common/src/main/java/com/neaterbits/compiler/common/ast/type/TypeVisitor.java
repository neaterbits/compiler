package com.neaterbits.compiler.common.ast.type;

import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.EnumType;
import com.neaterbits.compiler.common.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.ast.type.primitive.ArrayType;
import com.neaterbits.compiler.common.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.common.ast.type.primitive.ByteType;
import com.neaterbits.compiler.common.ast.type.primitive.Char16Type;
import com.neaterbits.compiler.common.ast.type.primitive.Char8Type;
import com.neaterbits.compiler.common.ast.type.primitive.DoubleType;
import com.neaterbits.compiler.common.ast.type.primitive.FloatType;
import com.neaterbits.compiler.common.ast.type.primitive.IntType;
import com.neaterbits.compiler.common.ast.type.primitive.LongType;
import com.neaterbits.compiler.common.ast.type.primitive.NullType;
import com.neaterbits.compiler.common.ast.type.primitive.ShortType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;
import com.neaterbits.compiler.common.ast.type.primitive.UnnamedVoidType;
import com.neaterbits.compiler.common.ast.type.primitive.NamedVoidType;

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
