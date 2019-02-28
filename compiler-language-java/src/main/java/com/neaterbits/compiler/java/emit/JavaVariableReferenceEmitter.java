package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.variables.StaticMemberReference;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.c.CLikeVariableReferenceEmitter;

public final class JavaVariableReferenceEmitter extends CLikeVariableReferenceEmitter<EmitterState> {

	@Override
	public Void onStaticMemberReference(StaticMemberReference staticMemberReference, EmitterState param) {

		final NamedType type = (NamedType)staticMemberReference.getClassType().getType();
		
		param.append(type.getName().getName());
		
		param.append('.');
		
		param.append(staticMemberReference.getName());
		
		return null;
	}

}
