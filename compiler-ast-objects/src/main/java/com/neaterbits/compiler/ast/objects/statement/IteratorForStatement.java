package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.ModifiersVariableDeclarationElement;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class IteratorForStatement extends LoopStatement {

	private final ASTSingle<ModifiersVariableDeclarationElement> variableDeclaration;
	private final ASTSingle<Expression> collectionExpression;

	public IteratorForStatement(
			Context context,
			ModifiersVariableDeclarationElement variableDeclaration,
			Expression collectionExpression,
			Block block) {
		
		super(context, block);
		
		Objects.requireNonNull(variableDeclaration);
		Objects.requireNonNull(collectionExpression);

		this.variableDeclaration = makeSingle(variableDeclaration);
		this.collectionExpression = makeSingle(collectionExpression);
	}

	public final ModifiersVariableDeclarationElement getVariableDeclaration() {
		return variableDeclaration.get();
	}

	public final Expression getCollectionExpression() {
		return collectionExpression.get();
	}
	
	public final TypeReference getTypeReference() {
	    return variableDeclaration.get().getTypeReference();
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onIteratorFor(this, param);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ITERATOR_FOR_STATEMENT;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		doIterate(variableDeclaration, recurseMode, iterator);
		doIterate(collectionExpression, recurseMode, iterator);

		super.doRecurse(recurseMode, iterator);
	}
}
