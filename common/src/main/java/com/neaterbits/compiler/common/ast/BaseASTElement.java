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
	
	protected abstract void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor);

	public final void iterateNodeFirst(ASTVisitor visitor) {
	
		visitor.onElement(this);
		
		doRecurse(ASTRecurseMode.VISIT_NODE_FIRST, visitor);
	}
	
	public final void iterateNodeLast(ASTVisitor visitor) {

		doRecurse(ASTRecurseMode.VISIT_NODE_LAST, visitor);
		
		visitor.onElement(this);
	}

	private static class ASTVisitorForStack implements ASTVisitor {
		
		private final Stack<BaseASTElement> stack;
		private final ASTStackVisitor stackVisitor;
		
		ASTVisitorForStack(ASTStackVisitor stackVisitor) {
			this.stack = new Stack<>();
			this.stackVisitor = stackVisitor;
		}

		@Override
		public void onElement(BaseASTElement element) {
			stackVisitor.onElement(element, stack);
		}
	}
	
	public final void iterateNodeFirstWithStack(ASTStackVisitor visitor) {
		iterateNodeFirst(new ASTVisitorForStack(visitor));
	}
	
	public final void iterateNodeLastWithStack(ASTStackVisitor visitor) {
		iterateNodeLast(new ASTVisitorForStack(visitor));
	}

	
	protected final void doIterate(ASTSingle<? extends ASTNode> single, ASTRecurseMode recurseMode, ASTVisitor visitor) {

		final ASTVisitorForStack stackVisitor = visitor instanceof ASTVisitorForStack
				? (ASTVisitorForStack)visitor
				: null;

		final BaseASTElement element = (BaseASTElement)single.get();
		
		if (stackVisitor != null) {
			stackVisitor.stack.push(element);
		}
		
		visit(element, recurseMode, visitor);
		
		if (stackVisitor != null) {
			stackVisitor.stack.pop();
		}
	}

	protected final void doIterate(ASTList<? extends ASTNode> list, ASTRecurseMode recurseMode, ASTVisitor visitor) {
		final ASTVisitorForStack stackVisitor = visitor instanceof ASTVisitorForStack
				? (ASTVisitorForStack)visitor
				: null;

		for (ASTNode node : list) {
			
			final BaseASTElement element = (BaseASTElement)node;
			
			if (stackVisitor != null) {
				stackVisitor.stack.push(element);
			}
			
			visit(element, recurseMode, visitor);

			if (stackVisitor != null) {
				stackVisitor.stack.pop();
			}
		}
	}
	
	private void visit(BaseASTElement element, ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
		switch (recurseMode) {
		case VISIT_NODE_FIRST:
			visitor.onElement(element);
			
			element.doRecurse(recurseMode, visitor);
			break;
			
		case VISIT_NODE_LAST:
			element.doRecurse(recurseMode, visitor);

			visitor.onElement(element);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown recurse mode " + recurseMode);
		}
	}
}
