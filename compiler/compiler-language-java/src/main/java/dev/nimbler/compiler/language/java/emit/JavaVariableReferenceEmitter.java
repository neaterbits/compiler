package dev.nimbler.compiler.language.java.emit;

import dev.nimbler.compiler.ast.objects.variables.StaticMemberReference;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.base.c.CLikeVariableReferenceEmitter;
import dev.nimbler.language.common.types.TypeName;

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
