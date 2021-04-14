package dev.nimbler.compiler.convert.ootofunction;

import dev.nimbler.compiler.ast.objects.type.BaseType;
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
import dev.nimbler.compiler.ast.objects.type.primitive.UnnamedVoidType;
import dev.nimbler.compiler.convert.ConverterState;
import dev.nimbler.compiler.convert.TypeConverter;

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
