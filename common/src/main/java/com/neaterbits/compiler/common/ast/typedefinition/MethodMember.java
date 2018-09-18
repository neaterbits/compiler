package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class MethodMember extends ComplexMemberDefinition {

	private final ASTSingle<MethodModifiers> modifiers;
	private final ASTSingle<Method> method;

	public MethodMember(Context context, MethodModifiers modifiers, Method method) {
		super(context);
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(method);
		
		this.modifiers = makeSingle(modifiers);
		this.method = makeSingle(method);
	}

	public MethodModifiers getModifiers() {
		return modifiers.get();
	}

	public Method getMethod() {
		return method.get();
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onMethodMember(this, param);
	}
}
