package dev.nimbler.compiler.emit.base.c;

import dev.nimbler.compiler.ast.objects.variables.ArrayAccessReference;
import dev.nimbler.compiler.ast.objects.variables.FieldAccessReference;
import dev.nimbler.compiler.ast.objects.variables.NameReference;
import dev.nimbler.compiler.ast.objects.variables.PrimaryListVariableReference;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.VariableReferenceEmitter;

public abstract class CLikeVariableReferenceEmitter<T extends EmitterState> implements VariableReferenceEmitter<T> {

	@Override
	public final Void onArrayAccessReference(ArrayAccessReference variableReference, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onFieldAccessReference(FieldAccessReference fieldAccessReference, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onPrimaryList(PrimaryListVariableReference primaryListVariableReference, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onNameReference(NameReference nameReference, T param) {
		
		param.append(nameReference.getName());
		
		return null;
	}
}
