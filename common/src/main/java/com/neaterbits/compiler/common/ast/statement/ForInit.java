package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
		if (localVariableDeclaration != null) {
			doIterate(localVariableDeclaration, recurseMode, visitor);
		}
		else if (expressionList != null) {
			doIterate(expressionList, recurseMode, visitor);
		}
	}
}
