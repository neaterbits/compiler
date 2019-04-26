package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.FieldNameDeclaration;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ClassDataFieldMember extends DataFieldMember {

	private final ASTSingle<FieldModifiers> modifiers; 
	private final ASTSingle<Expression> initializer;
	
	public ClassDataFieldMember(Context context, FieldModifiers modifiers, TypeReference type, FieldNameDeclaration name, Expression initializer) {
		super(context, type, name);
		
		this.modifiers = makeSingle(modifiers);
		this.initializer = initializer != null ? makeSingle(initializer) : null;
	}

	public Expression getInitializer() {
		return initializer != null ? initializer.get() : null;
	}

	public FieldModifiers getModifiers() {
		return modifiers.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassDataFieldMember(this, param);
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.FIELD;
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
