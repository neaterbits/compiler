package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassMethodMember(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
		doIterate(method, recurseMode, iterator);
	}
}
