package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.block.Constructor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public class ConstructorMember extends ComplexMemberDefinition {

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
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onConstructorMember(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(modifiers, recurseMode, visitor);
		doIterate(constructor, recurseMode, visitor);
	}
}
