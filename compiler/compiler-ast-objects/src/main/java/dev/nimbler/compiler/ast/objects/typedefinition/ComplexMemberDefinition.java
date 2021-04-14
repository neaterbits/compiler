package dev.nimbler.compiler.ast.objects.typedefinition;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.CompilationCode;

public abstract class ComplexMemberDefinition extends CompilationCode {

	protected abstract ComplexMemberType getMemberType();

	public final boolean isMethod() {
		return getMemberType() == ComplexMemberType.CLASS_METHOD || getMemberType() == ComplexMemberType.INTERFACE_METHOD;
	}
	
	protected ComplexMemberDefinition(Context context) {
		super(context);
	}
}
