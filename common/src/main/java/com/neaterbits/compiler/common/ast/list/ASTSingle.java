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
	
	@Override
	void onTake(ASTNode node) {
		
		Objects.requireNonNull(node);
		
		if (this.node != node) {
			throw new IllegalStateException("Removing other node");
		}
		
		this.node = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	ASTNodeHolder onReplace(ASTNode toRemove, ASTNode toAdd) {
		if (node != toRemove) {
			throw new IllegalArgumentException("Removing other node");
		}

		node = (T)toAdd;
		
		return this;
	}

	public void set(T node) {
		
		Objects.requireNonNull(node);

		if (node.nodeHolder != null) {
			throw new IllegalArgumentException("Node already in list");
		}

		
		if (this.node != null) {
			throw new IllegalStateException("Already set");
		}

		node.nodeHolder = this;
		
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
