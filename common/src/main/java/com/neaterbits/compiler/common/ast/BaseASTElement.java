package com.neaterbits.compiler.common.ast;


import java.util.function.Function;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.ArrayStack;
import com.neaterbits.compiler.common.StackView;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTNode;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public abstract class BaseASTElement extends ASTNode {
	
	private final Context context;

	public BaseASTElement(Context context) {
		this.context = context;
	}

	public final Context getContext() {
		return context;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
	protected abstract void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator);

	public final void iterateNodeFirst(ASTVisitor visitor) {
		
		final BaseASTIterator iterator = new BaseASTIterator() {
			@Override
			public void onElement(BaseASTElement element) {
				visitor.onElement(element);
			}
		};
		
		iterateNodeFirst(iterator);
	}

	private void iterateNodeFirst(ASTIterator iterator) {

		iterator.onElement(this);
		
		doRecurse(ASTRecurseMode.VISIT_NODE_FIRST, iterator);
	}
	
	public final void iterateNodeLast(ASTVisitor visitor) {

		final BaseASTIterator iterator = new BaseASTIterator() {
			@Override
			public void onElement(BaseASTElement element) {
				visitor.onElement(element);
			}
		};

		iterateNodeLast(iterator);
	}
	
	private void iterateNodeLast(ASTIterator iterator) {

		doRecurse(ASTRecurseMode.VISIT_NODE_LAST, iterator);
		
		iterator.onElement(this);
	}
	
	
	private static BaseASTElement returnElement(BaseASTElement element) {
		return element;
	}

	private static class ASTVisitorForStack<T, STACKVIEW extends StackView<T>, STACK extends Stack<T>> implements ASTIterator {
		
		private final ASTStackVisitor<T, STACKVIEW> stackVisitor;
		private final STACKVIEW stackView;
		private final STACK stack;
		private final Function<BaseASTElement, T> mapper;
		
		@SuppressWarnings("unchecked")
		ASTVisitorForStack(ASTStackVisitor<T, STACKVIEW> stackVisitor, STACK stack, Function<BaseASTElement, T> mapper) {
			this.stackVisitor = stackVisitor;
			this.stackView = (STACKVIEW)stack;
			this.stack = stack;
			this.mapper = mapper;
		}
		
		@Override
		public void onPush(BaseASTElement element) {
			stack.push(mapper.apply(element));
		}

		@Override
		public void onElement(BaseASTElement element) {
			stackVisitor.onElement(element, stackView);
		}

		@Override
		public void onPop(BaseASTElement element) {
			stack.pop();
		}
	}
	
	public final void iterateNodeFirstWithStack(ASTStackVisitor<BaseASTElement, StackView<BaseASTElement>> visitor) {
		iterateNodeFirst(new ASTVisitorForStack<>(visitor, new ArrayStack<>(), BaseASTElement::returnElement));
	}
	
	public final void iterateNodeLastWithStack(ASTStackVisitor<BaseASTElement, StackView<BaseASTElement>> visitor) {
		iterateNodeLast(new ASTVisitorForStack<>(visitor, new ArrayStack<>(), BaseASTElement::returnElement));
	}

	public final <STACK extends Stack<BaseASTElement>> void iterateNodeFirstWithStack(STACK stack, ASTVisitor visitor) {
		iterateNodeFirst(new ASTVisitorForStack<>((e, s) -> visitor.onElement(e), stack, BaseASTElement::returnElement));
	}
	
	public final <STACK extends Stack<BaseASTElement>> void iterateNodeLastWithStack(STACK stack, ASTVisitor visitor) {
		iterateNodeLast(new ASTVisitorForStack<>((e, s) -> visitor.onElement(e), stack, BaseASTElement::returnElement));
	}

	public final <T, STACK extends Stack<T>> void iterateNodeFirstWithStack(STACK stack, Function<BaseASTElement, T> mapper, ASTVisitor visitor) {
		iterateNodeFirst(new ASTVisitorForStack<>((e, s) -> visitor.onElement(e), stack, mapper));
	}
	
	public final <T, STACK extends Stack<T>> void iterateNodeLastWithStack(STACK stack, Function<BaseASTElement, T> mapper, ASTVisitor visitor) {
		iterateNodeLast(new ASTVisitorForStack<>((e, s) -> visitor.onElement(e), stack, mapper));
	}
	
	protected final void doIterate(ASTSingle<? extends ASTNode> single, ASTRecurseMode recurseMode, ASTIterator iterator) {

		final BaseASTElement element = (BaseASTElement)single.get();

		iterator.onPush(element);
		
		visit(element, recurseMode, iterator);

		iterator.onPop(element);
	}

	protected final void doIterate(ASTList<? extends ASTNode> list, ASTRecurseMode recurseMode, ASTIterator iterator) {

		for (ASTNode node : list) {
			
			final BaseASTElement element = (BaseASTElement)node;

			iterator.onPush(element);
			
			visit(element, recurseMode, iterator);

			iterator.onPop(element);
		}
	}
	
	private void visit(BaseASTElement element, ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		switch (recurseMode) {
		case VISIT_NODE_FIRST:
			iterator.onElement(element);
			
			element.doRecurse(recurseMode, iterator);
			break;
			
		case VISIT_NODE_LAST:
			element.doRecurse(recurseMode, iterator);

			iterator.onElement(element);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown recurse mode " + recurseMode);
		}
	}
}
