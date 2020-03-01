package com.neaterbits.compiler.emit.base;

import com.neaterbits.compiler.ast.objects.type.complex.ClassType;
import com.neaterbits.compiler.ast.objects.type.complex.EnumType;
import com.neaterbits.compiler.ast.objects.type.complex.InterfaceType;
import com.neaterbits.compiler.ast.objects.type.primitive.NullType;
import com.neaterbits.compiler.ast.objects.type.primitive.UnnamedVoidType;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.TypeEmitter;

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
