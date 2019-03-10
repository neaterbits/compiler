package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

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
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.INTERFACE_METHOD;
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
