package com.neaterbits.compiler.common.convert.ootofunction;

import com.neaterbits.compiler.common.ast.type.BaseType;
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
import com.neaterbits.compiler.common.ast.type.primitive.UnnamedVoidType;
import com.neaterbits.compiler.common.ast.type.primitive.NamedVoidType;
import com.neaterbits.compiler.common.convert.ConverterState;
import com.neaterbits.compiler.common.convert.TypeConverter;

public abstract class BaseTypeConverter<T extends ConverterState<T>>
	implements TypeConverter<T> {

	@Override
	public final BaseType onByte(ByteType type, T param) {
		return type;
	}

	@Override
	public final BaseType onShort(ShortType type, T param) {
		return type;
	}

	@Override
	public final BaseType onInt(IntType type, T param) {
		return type;
	}

	@Override
	public final BaseType onLong(LongType type, T param) {
		return type;
	}

	@Override
	public final BaseType onFloat(FloatType type, T param) {
		return type;
	}

	@Override
	public final BaseType onDouble(DoubleType type, T param) {
		return type;
	}

	@Override
	public final BaseType onChar8(Char8Type type, T param) {
		return type;
	}

	@Override
	public final BaseType onChar16(Char16Type type, T param) {
		return type;
	}

	@Override
	public final BaseType onBoolean(BooleanType type, T param) {
		return type;
	}

	@Override
	public final BaseType onVoid(NamedVoidType type, T param) {
		return type;
	}

	@Override
	public final BaseType onNullType(NullType type, T param) {
		return type;
	}
	
	@Override
	public final BaseType onUnnamedVoidType(UnnamedVoidType type, T param) {
		return type;
	}

	@Override
	public BaseType onArray(ArrayType type, T param) {
		return type;
	}

}
