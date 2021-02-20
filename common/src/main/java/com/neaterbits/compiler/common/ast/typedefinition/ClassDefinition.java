package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;

public final class ClassDefinition extends BaseClassDefinition {
	
	public ClassDefinition(Context context, ClassModifiers modifiers, ClassName name,
			List<ComplexMemberDefinition> members) {
		super(context, modifiers, name, members);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassDefinition(this, param);
	}
}
