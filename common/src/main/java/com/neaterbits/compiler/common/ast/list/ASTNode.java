package com.neaterbits.compiler.common.ast.list;

import java.util.Collection;
import java.util.Iterator;

public abstract class ASTNode implements Iterator<ASTNode> {

	ASTNodeHolder nodeHolder;
	
	ASTNode next;
	ASTNode last;
	
	public final void remove() {

		if (nodeHolder == null) {
			throw new IllegalStateException("Not in a list");
		}
		
		nodeHolder.onRemove(this);
		
		this.nodeHolder = null;

		this.last.next = this.next;
		this.next.last = this.last;

		this.last = null;
		this.next = null;
	}

	@Override
	public final boolean hasNext() {
		// next.next since always is a tail element in list
		return this.next.next != null;
	}

	@Override
	public final ASTNode next() {
		return this.next;
	}

	public final <T extends ASTNode> ASTList<T> makeList(Collection<T> collection) {
		return new ASTMutableList<>(collection);
	}
	
	public final <T extends ASTNode> ASTSingle<T> makeSingle(T element) {
		return new ASTSingle<>(element);
	}
}
