package com.neaterbits.compiler.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class InnerClassMember extends ComplexMemberDefinition {

	private final ASTSingle<ClassDefinition> classDefinition;
	
	public InnerClassMember(Context context, ClassDefinition classDefinition) {
		super(context);
		
		Objects.requireNonNull(classDefinition);
		
		this.classDefinition = makeSingle(classDefinition);
	}

	public ClassDefinition getClassDefinition() {
		return classDefinition.get();
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.INNER_CLASS;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInnerClassMember(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(classDefinition, recurseMode, iterator);
	}
}