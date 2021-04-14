package com.neaterbits.compiler.ast.objects.list;

public abstract class ASTNodeHolder {

	abstract void onRemove(ASTNode node);

	abstract void onTake(ASTNode node);
	
	abstract ASTNodeHolder onReplace(ASTNode toRemove, ASTNode toAdd);
}
