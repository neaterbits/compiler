package dev.nimbler.compiler.ast.objects;

import org.jutils.StackView;;

public interface ASTStackVisitor<T, STACK extends StackView<T>> {

	void onElement(BaseASTElement element, STACK stack);
	
}
