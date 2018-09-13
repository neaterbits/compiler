package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.block.Method;

public final class MethodMember extends ComplexMemberDefinition {

	private final Method method;

	public MethodMember(Context context, Method method) {
		super(context);
		
		Objects.requireNonNull(method);
		
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onMethodMember(this, param);
	}
}
