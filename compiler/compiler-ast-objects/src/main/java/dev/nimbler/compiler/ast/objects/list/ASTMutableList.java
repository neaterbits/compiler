package dev.nimbler.compiler.ast.objects.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

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

		if (node.nodeHolder != null) {
			throw new IllegalArgumentException("Node already in list");
		}

		node.last = tail.last;
		node.next = tail;

		tail.last.next = node;
		tail.last = node;

		++ size;
		
		node.nodeHolder = this;
	}
	
	private void removeNode(ASTNode node) {
		Objects.requireNonNull(node);

		-- size;

		node.last.next = node.next;
		node.next.last = node.last;
	}

	@Override
	void onRemove(ASTNode node) {
		removeNode(node);
	}
	
	@Override
	void onTake(ASTNode node) {
		removeNode(node);
	}

	@Override
	ASTNodeHolder onReplace(ASTNode toRemove, ASTNode toAdd) {

		toAdd.last = toRemove.last;
		toAdd.next = toRemove.next;
		
		toRemove.last.next = toAdd;
		toRemove.next.last = toAdd;

		return this;
	}

	@Override
	public Iterator<T> iterator() {
		// return (Iterator)head;
		
		return new IteratorImpl<>(head);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getLast() {
		if (size == 0) {
			throw new IllegalStateException("Empty list");
		}
		
		return (T)tail.last;
	}

	@Override
	public final int size() {
		return size;
	}
	
	@SuppressWarnings("unchecked")
	T getAt(int index) {
		
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
	

	@SuppressWarnings("unchecked")
	@Override
	public T find(Predicate<T> predicate) {
		for (ASTNode node = head; node.next.next != null; node = node.next) {
			
			final T value = (T)node.next;
			
			if (predicate.test(value)) {
				return value;
			}
		}

		return null;
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
			}

			sb.append(node.toString());
		});
		
		sb.append(']');
		
		return sb.toString();
	}
}
