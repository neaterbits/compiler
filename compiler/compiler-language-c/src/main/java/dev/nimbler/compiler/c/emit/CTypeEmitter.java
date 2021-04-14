package dev.nimbler.compiler.c.emit;

import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.compiler.ast.objects.type.NamedType;
import dev.nimbler.compiler.ast.objects.type.PointerType;
import dev.nimbler.compiler.ast.objects.type.TypeDefType;
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
import dev.nimbler.compiler.ast.objects.type.primitive.ShortType;
import dev.nimbler.compiler.ast.objects.type.primitive.StringType;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.base.BaseProceduralTypeEmitter;

public class CTypeEmitter extends BaseProceduralTypeEmitter<EmitterState> {

	private void emitType(BaseType type, EmitterState param) {
		type.visit(this, param);
	}

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
		
		final NamedType delegate = (NamedType)type.getDelegate();
		
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
	public Void onTypeDef(TypeDefType type, EmitterState param) {
		
		param.append(type.getName().getName());
		
		return null;
	}

	@Override
	public Void onStruct(StructType type, EmitterState param) {
		
		param.append("struct ").append(type.getName().getName());
		
		return null;
	}
	@Override
	public Void onFunctionPointer(FunctionPointerType type, EmitterState param) {
		
		emitType(type.getReturnType(), param);
		
		param.append("(*)(");

		emitListTo(param, type.getParameters(), ", ", parameter -> {
			emitType(parameter.getType(), param);
			
			if (parameter.getName() != null) {
				param.append(' ').append(parameter.getName().getName());
			}
		});
		
		param.append(')');
		
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
	public Void onArray(ArrayType type, EmitterState param) {
		
		emitType(type.getElementType(), param);
		
		for (int i = 0; i < type.getNumDims(); ++ i) {
			param.append("[]");
		}
		
		return null;
	}

	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		throw new UnsupportedOperationException();
	}
}
