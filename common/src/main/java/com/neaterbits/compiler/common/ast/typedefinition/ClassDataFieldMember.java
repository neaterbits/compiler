package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public class ClassDataFieldMember extends DataFieldMember {

	private final ASTSingle<FieldModifiers> modifiers; 
	private final ASTSingle<Expression> initializer;
	
	public ClassDataFieldMember(Context context, FieldModifiers modifiers, TypeReference type, FieldName name, Expression initializer) {
		super(context, type, name);
		
		this.modifiers = makeSingle(modifiers);
		this.initializer = makeSingle(initializer);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassDataFieldMember(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(modifiers, recurseMode, visitor);

		super.doRecurse(recurseMode, visitor);

		doIterate(initializer, recurseMode, visitor);
	}
}
