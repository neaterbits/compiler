package dev.nimbler.compiler.ast.objects.list;

@FunctionalInterface
public interface ASTListIndexIterator<T> {

	void each(T element, int index);
	
}
