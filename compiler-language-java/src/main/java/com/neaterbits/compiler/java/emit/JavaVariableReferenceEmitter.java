package com.neaterbits.compiler.java.emit;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.objects.variables.StaticMemberReference;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.base.c.CLikeVariableReferenceEmitter;

public final class JavaVariableReferenceEmitter extends CLikeVariableReferenceEmitter<EmitterState> {

	@Override
	public Void onStaticMemberReference(StaticMemberReference staticMemberReference, EmitterState param) {

		final TypeName typeName = staticMemberReference.getClassType().getTypeName();
		
		param.append(typeName.getName());
		
		param.append('.');
		
		param.append(staticMemberReference.getName());
		
		return null;
	}

}
