package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.variables.ModifiersVariableDeclarationElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
