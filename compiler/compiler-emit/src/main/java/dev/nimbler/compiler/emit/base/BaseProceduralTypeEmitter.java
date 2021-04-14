package dev.nimbler.compiler.emit.base;

import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.type.complex.EnumType;
import dev.nimbler.compiler.ast.objects.type.complex.InterfaceType;
import dev.nimbler.compiler.ast.objects.type.primitive.NullType;
import dev.nimbler.compiler.ast.objects.type.primitive.UnnamedVoidType;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.TypeEmitter;

public abstract class BaseProceduralTypeEmitter<T extends EmitterState>
		extends BaseEmitter<T>
		implements TypeEmitter<T> {

	@Override
	public final Void onClass(ClassType type, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onInterface(InterfaceType type, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onEnum(EnumType type, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onNullType(NullType type, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onUnnamedVoidType(UnnamedVoidType type, T param) {
		throw new UnsupportedOperationException();
	}
}
