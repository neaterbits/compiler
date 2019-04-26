package com.neaterbits.compiler.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ClassMethodMember extends ComplexMemberDefinition {

	private final ASTSingle<ClassMethodModifiers> modifiers;
	private final ASTSingle<ClassMethod> method;

	public ClassMethodMember(Context context, ClassMethodModifiers modifiers, ClassMethod method) {
		super(context);
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(method);
		
		this.modifiers = makeSingle(modifiers);
		this.method = makeSingle(method);
	}

	public ClassMethodModifiers getModifiers() {
		return modifiers.get();
	}

	public ClassMethod getMethod() {
		return method.get();
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.CLASS_METHOD;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD_MEMBER;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassMethodMember(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
		doIterate(method, recurseMode, iterator);
	}
}
