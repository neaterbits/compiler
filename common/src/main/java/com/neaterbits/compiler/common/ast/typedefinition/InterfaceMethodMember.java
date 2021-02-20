package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class InterfaceMethodMember extends ComplexMemberDefinition {

	private final ASTSingle<InterfaceMethodModifiers> modifiers;
	private final ASTSingle<InterfaceMethod> method;
	
	public InterfaceMethodMember(Context context, InterfaceMethodModifiers modifiers, InterfaceMethod method) {
		super(context);

		this.modifiers = makeSingle(modifiers);
		this.method = makeSingle(method);
	}

	public InterfaceMethodModifiers getModifiers() {
		return modifiers.get();
	}

	public InterfaceMethod getMethod() {
		return method.get();
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInterfaceMethodMember(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(modifiers, recurseMode, iterator);
		doIterate(method, recurseMode, iterator);
	}
}
