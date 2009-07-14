package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class ClassDataFieldMember extends DataFieldMember {

	private final ASTSingle<FieldModifiers> modifiers; 
	private final ASTSingle<Expression> initializer;
	
	public ClassDataFieldMember(Context context, FieldModifiers modifiers, TypeReference type, FieldName name, Expression initializer) {
		super(context, type, name);
		
		this.modifiers = makeSingle(modifiers);
		this.initializer = initializer != null ? makeSingle(initializer) : null;
	}

	public Expression getInitializer() {
		return initializer != null ? initializer.get() : null;
	}
	
	
	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassDataFieldMember(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(modifiers, recurseMode, iterator);

		super.doRecurse(recurseMode, iterator);

		if (initializer != null) {
			doIterate(initializer, recurseMode, iterator);
		}
	}
}
