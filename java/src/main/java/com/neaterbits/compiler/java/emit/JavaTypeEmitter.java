package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.common.ast.type.PointerType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.common.ast.type.primitive.ByteType;
import com.neaterbits.compiler.common.ast.type.primitive.Char16Type;
import com.neaterbits.compiler.common.ast.type.primitive.Char8Type;
import com.neaterbits.compiler.common.ast.type.primitive.DoubleType;
import com.neaterbits.compiler.common.ast.type.primitive.FloatType;
import com.neaterbits.compiler.common.ast.type.primitive.IntType;
import com.neaterbits.compiler.common.ast.type.primitive.LongType;
import com.neaterbits.compiler.common.ast.type.primitive.ShortType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.TypeEmitter;

public final class JavaTypeEmitter implements TypeEmitter<EmitterState> {

	@Override
	public Void onByte(ByteType type, EmitterState param) {
		param.append("byte");
		
		return null;
	}

	@Override
	public Void onChar8(Char8Type type, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onChar16(Char16Type type, EmitterState param) {
		param.append("char");
		
		return null;
	}

	@Override
	public Void onShort(ShortType type, EmitterState param) {
		param.append("short");
		
		return null;
	}

	@Override
	public Void onInt(IntType type, EmitterState param) {
		param.append("int");

		return null;
	}

	@Override
	public Void onLong(LongType type, EmitterState param) {
		param.append("long");
		
		return null;
	}

	@Override
	public Void onFloat(FloatType type, EmitterState param) {
		param.append("float");

		return null;
	}

	@Override
	public Void onDouble(DoubleType type, EmitterState param) {
		param.append("double");

		return null;
	}

	
	@Override
	public Void onBoolean(BooleanType type, EmitterState param) {
		param.append("bool");

		return null;
	}

	@Override
	public Void onString(StringType type, EmitterState param) {
		param.append("string");
		
		return null;
	}

	@Override
	public Void onPointer(PointerType type, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onClass(ClassType type, EmitterState param) {
		param.append(type.getName().getName());
		
		return null;
	}

	@Override
	public Void onStruct(StructType type, EmitterState param) {
		throw new UnsupportedOperationException();
	}
}

