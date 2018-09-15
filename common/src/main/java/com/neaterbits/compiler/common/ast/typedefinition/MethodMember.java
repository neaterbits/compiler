package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.block.Method;

public final class MethodMember extends ComplexMemberDefinition {

	private final MethodModifiers modifiers;
	private final Method method;

	public MethodMember(Context context, MethodModifiers modifiers, Method method) {
		super(context);
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(method);
		
		this.modifiers = modifiers;
		this.method = method;
	}

	public MethodModifiers getModifiers() {
		return modifiers;
	}

	public Method getMethod() {
		return method;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onMethodMember(this, param);
	}
}
