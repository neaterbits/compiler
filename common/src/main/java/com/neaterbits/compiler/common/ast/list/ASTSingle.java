package com.neaterbits.compiler.common.ast.list;

import java.util.Objects;

public final class ASTSingle<T extends ASTNode> extends ASTNodeHolder {

	private T node;
	
	public ASTSingle(T node) {
		set(node);
	}
	
	@Override
	void onRemove(ASTNode node) {
		throw new IllegalStateException("Removing from single-node");
	}

	public void set(T node) {
		
		Objects.requireNonNull(node);

		if (this.node != null) {
			throw new IllegalStateException("Already set");
		}

		this.node = node;
	}
	
	public T get() {
		return node;
	}

	@Override
	public String toString() {
		return node.toString();
	}
}
