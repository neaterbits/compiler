package com.neaterbits.compiler.common.ast;


import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.Stack;
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

	private static class ASTVisitorForStack implements ASTIterator {
		
		private final Stack<BaseASTElement> stack;
		private final ASTStackVisitor stackVisitor;
		
		ASTVisitorForStack(ASTStackVisitor stackVisitor) {
			this.stack = new Stack<>();
			this.stackVisitor = stackVisitor;
		}

		@Override
		public void onPush(BaseASTElement element) {
			stack.push(element);
		}

		@Override
		public void onElement(BaseASTElement element) {
			stackVisitor.onElement(element, stack);
		}


		@Override
		public void onPop(BaseASTElement element) {
			stack.pop();
		}
	}
	
	public final void iterateNodeFirstWithStack(ASTStackVisitor visitor) {
		iterateNodeFirst(new ASTVisitorForStack(visitor));
	}
	
	public final void iterateNodeLastWithStack(ASTStackVisitor visitor) {
		iterateNodeLast(new ASTVisitorForStack(visitor));
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
