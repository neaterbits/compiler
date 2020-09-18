package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.types.ParseTreeElement;
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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_METHOD_MEMBER;
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
