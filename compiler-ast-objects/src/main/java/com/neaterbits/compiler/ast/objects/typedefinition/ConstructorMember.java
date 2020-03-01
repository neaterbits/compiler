package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.block.Constructor;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTRUCTOR_MEMBER;
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
