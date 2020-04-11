package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.literal.Primary;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ThisPrimary extends Primary {

	private final ASTSingle<TypeReference> type;
	
	public ThisPrimary(Context context, TypeReference type) {
		super(context);
		
		Objects.requireNonNull(type);
	
		this.type = makeSingle(type);
	}

	@Override
	public TypeReference getType() {
		return type.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.THIS_PRIMARY;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onThis(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}