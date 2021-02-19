package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(modifiers, recurseMode, visitor);
		doIterate(method, recurseMode, visitor);
	}
}
