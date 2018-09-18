package com.neaterbits.compiler.common.ast.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public final class ASTMutableList<T extends ASTNode> extends ASTList<T> {

	private static class ASTHead extends ASTNode {
		
	}
	
	private static class ASTTail extends ASTNode {
		
	}
	
	private class IteratorImpl<TT> implements Iterator<TT> {
		private ASTNode cur;

		IteratorImpl(ASTNode cur) {
			this.cur = cur;
		}
		
		@Override
		public boolean hasNext() {
			return cur.hasNext();
		}

		@SuppressWarnings("unchecked")
		@Override
		public TT next() {
			cur = cur.next;
			
			return (TT)cur;
		}
	}
	
	private int size;
	private final ASTHead head;
	private final ASTTail tail;
	
	public ASTMutableList() {
		
		this.size = 0;
		
		this.head = new ASTHead();
		this.tail = new ASTTail();
		
		head.last = null;
		head.next = tail;
		
		tail.last = head;
		tail.next = null;
	}
	
	ASTMutableList(Collection<T> collection) {
		this();
		
		for (ASTNode node : collection) {
			add(node);
		}
	}

	public final void add(ASTNode node) {
		Objects.requireNonNull(node);

		node.last = tail.last;
		node.next = tail;

		tail.last.next = node;
		tail.last = node;

		++ size;
		
		node.nodeHolder = this;
	}
	

	@Override
	void onRemove(ASTNode node) {
		Objects.requireNonNull(node);
		
		-- size;
	}

	@Override
	public Iterator<T> iterator() {
		// return (Iterator)head;
		
		return new IteratorImpl<>(head);
	}

	@Override
	public final int size() {
		return size;
	}
	
	@SuppressWarnings("unchecked")
	T get(int index) {
		
		if (index < 0) {
			throw new IllegalArgumentException("index < 0");
		}

		if (index >= size) {
			throw new IllegalArgumentException("index >= size");
		}
		
		int i = 0;
		
		for (ASTNode node = head; node.hasNext(); node = node.next()) {
			if (i ++ == index) {
				return (T)node;
			}
		}

		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void foreachWithIndex(ASTListIndexIterator<T> function) {
		int i = 0;
		
		for (ASTNode node = head; node.next.next != null; node = node.next) {
			
			function.each((T)node.next, i ++);
		}
	}

	public final boolean isEmpty() {
		return size == 0;
	}
	
	public final ASTNode getFirst() {
		if (isEmpty()) {
			throw new IllegalStateException("list is empty");
		}
		
		return head.next;
	}

	@Override
	public String toString() {
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append('[');
		
		foreachWithIndex((node, i) -> {
			if (i > 0) {
				sb.append(',');
				
				sb.append(node.toString());
			}
		});
		
		sb.append(']');
		
		return sb.toString();
	}
}
