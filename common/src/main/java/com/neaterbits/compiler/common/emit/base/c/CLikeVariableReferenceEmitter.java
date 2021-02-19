package com.neaterbits.compiler.common.emit.base.c;

import com.neaterbits.compiler.common.ast.variables.ArrayAccessReference;
import com.neaterbits.compiler.common.ast.variables.FieldAccessReference;
import com.neaterbits.compiler.common.ast.variables.NameReference;
import com.neaterbits.compiler.common.ast.variables.PrimaryListVariableReference;
import com.neaterbits.compiler.common.ast.variables.SimpleVariableReference;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.VariableReferenceEmitter;

public abstract class CLikeVariableReferenceEmitter<T extends EmitterState> implements VariableReferenceEmitter<T> {

	@Override
	public final Void onSimpleVariableReference(SimpleVariableReference variableReference, T param) {
		
		param.append(variableReference.getName().getName());
		
		return null;
	}

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
