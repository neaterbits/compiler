package com.neaterbits.compiler.ast.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class ASTNode implements Iterator<ASTNode> {

	ASTNodeHolder nodeHolder;
	
	ASTNode next;
	ASTNode last;
	
	private StackTraceElement [] removedAt;
	private String removeOp;
	
	public final void remove() {
		remove(nodeHolder::onRemove);
	}
	
	@SuppressWarnings("unchecked")
	public final <T> T take() {
		remove(nodeHolder::onTake);
		
		return (T)this;
	}
	
	public final boolean isInList() {
		return nodeHolder != null;
	}

	private void remove(Consumer<ASTNode> onRemoved) {
		if (nodeHolder == null) {
			printDebug();
			
			throw new IllegalStateException("Not in a list");
		}
		
		onRemoved.accept(this);
		
		this.nodeHolder = null;
		
		this.removedAt = Thread.currentThread().getStackTrace();
		this.removeOp = "remove";

		/* Iterators fail if set to null
		this.last = null;
		this.next = null;
		*/
	}
	
	public final void replaceWith(ASTNode node) {
		Objects.requireNonNull(node);
		
		if (node == this) {
			throw new IllegalArgumentException("replacing with same");
		}

		if (node.nodeHolder != null) {
			throw new IllegalArgumentException("Already in list");
		}
		
		if (this.nodeHolder == null) {
			printDebug();
			
			throw new IllegalStateException("Not in list: " + this.getClass().getSimpleName());
		}
		
		node.nodeHolder = nodeHolder.onReplace(this, node);

		if (node.nodeHolder == null) {
			throw new IllegalStateException();
		}
		
		this.nodeHolder = null;

		this.removedAt = Thread.currentThread().getStackTrace();
		this.removeOp = "remove";

		/* Iterators fail if set to null
		this.last = null;
		this.next = null;
		*/
	}
	
	private void printDebug() {
		System.out.println("removeOp: " + removeOp);
		
		for (int i = 0; i < 5; ++ i) {
			System.out.println(removedAt[i].getMethodName());
		}
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
