package com.neaterbits.compiler.common.ast.type;

import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.ast.type.primitive.ByteType;
import com.neaterbits.compiler.common.ast.type.primitive.Char16Type;
import com.neaterbits.compiler.common.ast.type.primitive.Char8Type;
import com.neaterbits.compiler.common.ast.type.primitive.DoubleType;
import com.neaterbits.compiler.common.ast.type.primitive.FloatType;
import com.neaterbits.compiler.common.ast.type.primitive.IntType;
import com.neaterbits.compiler.common.ast.type.primitive.LongType;
import com.neaterbits.compiler.common.ast.type.primitive.ShortType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;

public interface TypeVisitor<T, R> {
	
	R onByte(ByteType type, T param);
	
	R onChar8(Char8Type type, T param);
	
	R onChar16(Char16Type type, T param);
	
	R onShort(ShortType type, T param);

	R onInt(IntType type, T param);

	R onLong(LongType type, T param);

	R onFloat(FloatType type, T param);

	R onDouble(DoubleType type, T param);
	
	R onString(StringType type, T param);
	
	R onPointer(PointerType type, T param);

	R onClass(ClassType type, T param);

	R onStruct(StructType type, T param);
}
