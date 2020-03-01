package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ForInit extends BaseASTElement {

	private final ASTSingle<InitializerVariableDeclarationElement> localVariableDeclaration;
	private final ASTSingle<ForExpressionList> expressionList;
	
	public ForInit(Context context, InitializerVariableDeclarationElement localVariableDeclaration) {
		super(context);
		
		Objects.requireNonNull(localVariableDeclaration);
		
		this.localVariableDeclaration = makeSingle(localVariableDeclaration);
		this.expressionList = null;
	}

	public ForInit(Context context, ForExpressionList expressionList) {
		super(context);
		
		Objects.requireNonNull(expressionList);
		
		this.localVariableDeclaration = null;
		this.expressionList = makeSingle(expressionList);
	}
	
	public InitializerVariableDeclarationElement getLocalVariableDeclaration() {
		return localVariableDeclaration != null ? localVariableDeclaration.get() : null;
	}

	public ForExpressionList getExpressionList() {
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
