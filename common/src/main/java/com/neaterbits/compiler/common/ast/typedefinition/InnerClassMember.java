package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInnerClassMember(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(classDefinition, recurseMode, visitor);
	}
}
