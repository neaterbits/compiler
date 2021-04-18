package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ForInit extends BaseASTElement {

	private final ASTSingle<VariableDeclarationStatement> localVariableDeclaration;
	private final ASTSingle<ForUpdateExpressionList> expressionList;
	
	public ForInit(Context context, VariableDeclarationStatement localVariableDeclaration) {
		super(context);
		
		Objects.requireNonNull(localVariableDeclaration);
		
		this.localVariableDeclaration = makeSingle(localVariableDeclaration);
		this.expressionList = null;
	}

	public ForInit(Context context, ForUpdateExpressionList expressionList) {
		super(context);
		
		Objects.requireNonNull(expressionList);
		
		this.localVariableDeclaration = null;
		this.expressionList = makeSingle(expressionList);
	}
	
	public VariableDeclarationStatement getLocalVariableDeclaration() {
		return localVariableDeclaration != null ? localVariableDeclaration.get() : null;
	}

	public ForUpdateExpressionList getExpressionList() {
		return expressionList != null ? expressionList.get() : null;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FOR_INIT;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		if (localVariableDeclaration != null) {
			doIterate(localVariableDeclaration, recurseMode, iterator);
		}
		else if (expressionList != null) {
			doIterate(expressionList, recurseMode, iterator);
		}
	}
}
