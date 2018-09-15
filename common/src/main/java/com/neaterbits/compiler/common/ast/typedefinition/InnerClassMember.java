package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;

public final class InnerClassMember extends ComplexMemberDefinition {

	private final ClassDefinition classDefinition;
	
	public InnerClassMember(Context context, ClassDefinition classDefinition) {
		super(context);
		
		Objects.requireNonNull(classDefinition);
		
		this.classDefinition = classDefinition;
	}

	public ClassDefinition getClassDefinition() {
		return classDefinition;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInnerClassMember(this, param);
	}
}
