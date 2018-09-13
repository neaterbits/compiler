package com.neaterbits.compiler.c.emit;

import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.PointerType;
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
import com.neaterbits.compiler.common.emit.BaseProceduralTypeEmitter;
import com.neaterbits.compiler.common.emit.EmitterState;

public class CTypeEmitter extends BaseProceduralTypeEmitter<EmitterState> {

	@Override
	public Void onByte(ByteType type, EmitterState param) {
		
		param.append("int8_t");
		
		return null;
	}

	@Override
	public Void onShort(ShortType type, EmitterState param) {

		param.append("int16_t");

		return null;
	}

	@Override
	public Void onInt(IntType type, EmitterState param) {
		param.append("int32_t");

		return null;
	}

	@Override
	public Void onLong(LongType type, EmitterState param) {
		param.append("int64_t");

		return null;
	}

	@Override
	public Void onFloat(FloatType type, EmitterState param) {
		param.append("float32_t");
		
		return null;
	}

	@Override
	public Void onDouble(DoubleType type, EmitterState param) {
		param.append("float64_t");

		return null;
	}

	@Override
	public Void onString(StringType type, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onPointer(PointerType type, EmitterState param) {
		
		final NamedType delegate = (NamedType)type.getDelegate().getType();
		
		if (delegate instanceof StructType) {
			param.append("struct ");
		}

		param.append(delegate.getName().getName());
		param.append(' ');
		
		for (int i = 0; i < type.getLevels(); ++ i) {
			param.append('*');
		}
		
		return null;
	}

	@Override
	public Void onStruct(StructType type, EmitterState param) {
		
		param.append("struct ").append(type.getName().getName());
		
		return null;
	}

	@Override
	public Void onChar8(Char8Type type, EmitterState param) {
		
		param.append("char");
		
		return null;
	}

	@Override
	public Void onChar16(Char16Type type, EmitterState param) {
		param.append("char16_t");

		return null;
	}
}
