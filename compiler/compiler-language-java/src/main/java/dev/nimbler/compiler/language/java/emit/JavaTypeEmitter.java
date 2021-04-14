package dev.nimbler.compiler.language.java.emit;

import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.compiler.ast.objects.type.PointerType;
import dev.nimbler.compiler.ast.objects.type.TypeDefType;
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
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.TypeEmitter;

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

