package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.block.Constructor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class ConstructorMember extends ComplexMemberDefinition {

	private final ASTSingle<ConstructorModifiers> modifiers;
	private final ASTSingle<Constructor> constructor;
	
	public ConstructorMember(Context context, ConstructorModifiers modifiers, Constructor constructor) {
		super(context);
		
		Objects.requireNonNull(modifiers);
		Objects.requireNonNull(constructor);
		
		this.modifiers = makeSingle(modifiers);
		this.constructor = makeSingle(constructor);
	}

	public ConstructorModifiers getModifiers() {
		return modifiers.get();
	}

	public Constructor getConstructor() {
		return constructor.get();
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.CONSTRUCTOR;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onConstructorMember(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
		doIterate(constructor, recurseMode, iterator);
	}
}
