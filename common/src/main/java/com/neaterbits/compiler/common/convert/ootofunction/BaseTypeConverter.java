package com.neaterbits.compiler.common.convert.ootofunction;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ResolvedTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.common.ast.type.primitive.ByteType;
import com.neaterbits.compiler.common.ast.type.primitive.Char16Type;
import com.neaterbits.compiler.common.ast.type.primitive.Char8Type;
import com.neaterbits.compiler.common.ast.type.primitive.DoubleType;
import com.neaterbits.compiler.common.ast.type.primitive.FloatType;
import com.neaterbits.compiler.common.ast.type.primitive.IntType;
import com.neaterbits.compiler.common.ast.type.primitive.LongType;
import com.neaterbits.compiler.common.ast.type.primitive.ShortType;

public abstract class BaseTypeConverter
	implements TypeVisitor<Context, TypeReference> {

	@Override
	public final TypeReference onByte(ByteType type, Context context) {
		return new ResolvedTypeReference(context, type);
	}

	@Override
	public final TypeReference onShort(ShortType type, Context context) {
		return new ResolvedTypeReference(context, type);
	}

	@Override
	public final TypeReference onInt(IntType type, Context context) {
		return new ResolvedTypeReference(context, type);
	}

	@Override
	public final TypeReference onLong(LongType type, Context context) {
		return new ResolvedTypeReference(context, type);
	}

	@Override
	public final TypeReference onFloat(FloatType type, Context context) {
		return new ResolvedTypeReference(context, type);
	}

	@Override
	public final TypeReference onDouble(DoubleType type, Context context) {
		return new ResolvedTypeReference(context, type);
	}

	@Override
	public final TypeReference onChar8(Char8Type type, Context context) {
		return new ResolvedTypeReference(context, type);
	}

	@Override
	public final TypeReference onChar16(Char16Type type, Context context) {
		return new ResolvedTypeReference(context, type);
	}

	@Override
	public TypeReference onBoolean(BooleanType type, Context context) {
		return new ResolvedTypeReference(context, type);
	}
}
