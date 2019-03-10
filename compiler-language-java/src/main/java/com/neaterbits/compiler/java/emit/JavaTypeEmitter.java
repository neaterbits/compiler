package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.FunctionPointerType;
import com.neaterbits.compiler.ast.type.PointerType;
import com.neaterbits.compiler.ast.type.TypeDefType;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.EnumType;
import com.neaterbits.compiler.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.ast.type.complex.StructType;
import com.neaterbits.compiler.ast.type.primitive.ArrayType;
import com.neaterbits.compiler.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.ast.type.primitive.ByteType;
import com.neaterbits.compiler.ast.type.primitive.Char16Type;
import com.neaterbits.compiler.ast.type.primitive.Char8Type;
import com.neaterbits.compiler.ast.type.primitive.DoubleType;
import com.neaterbits.compiler.ast.type.primitive.FloatType;
import com.neaterbits.compiler.ast.type.primitive.IntType;
import com.neaterbits.compiler.ast.type.primitive.LongType;
import com.neaterbits.compiler.ast.type.primitive.NamedVoidType;
import com.neaterbits.compiler.ast.type.primitive.NullType;
import com.neaterbits.compiler.ast.type.primitive.ShortType;
import com.neaterbits.compiler.ast.type.primitive.StringType;
import com.neaterbits.compiler.ast.type.primitive.UnnamedVoidType;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.TypeEmitter;

public final class JavaTypeEmitter implements TypeEmitter<EmitterState> {

	private void emitType(BaseType type, EmitterState param) {
		type.visit(this, param);
	}
	
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
	public Void onVoid(NamedVoidType type, EmitterState param) {
		param.append("void");

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
	public Void onInterface(InterfaceType type, EmitterState param) {
		param.append(type.getName().getName());

		return null;
	}

	@Override
	public Void onEnum(EnumType type, EmitterState param) {
		param.append(type.getName().getName());

		return null;
	}

	@Override
	public Void onArray(ArrayType type, EmitterState param) {
		
		emitType(type.getElementType(), param);
		
		for (int i = 0; i < type.getNumDims(); ++ i) {
			param.append("[]");
		}
		
		return null;
	}

	@Override
	public Void onStruct(StructType type, EmitterState param) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Void onFunctionPointer(FunctionPointerType type, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onNullType(NullType type, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onUnnamedVoidType(UnnamedVoidType type, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onTypeDef(TypeDefType type, EmitterState param) {
		throw new UnsupportedOperationException();
	}
}

