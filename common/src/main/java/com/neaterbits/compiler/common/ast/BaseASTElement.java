package com.neaterbits.compiler.common.ast;


import com.neaterbits.compiler.common.Context;
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
	
	public abstract void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor);

	public final void iterateNodeFirst(ASTVisitor visitor) {
	
		visitor.onElement(this);
		
		doRecurse(ASTRecurseMode.VISIT_NODE_FIRST, visitor);
	}
	
	public final void iterateNodeLast(ASTVisitor visitor) {

		doRecurse(ASTRecurseMode.VISIT_NODE_LAST, visitor);
		
		visitor.onElement(this);
	}
	
	public final void doIterate(ASTSingle<? extends ASTNode> single, ASTRecurseMode recurseMode, ASTVisitor visitor) {
		visit(single.get(), recurseMode, visitor);
	}

	public final void doIterate(ASTList<? extends ASTNode> list, ASTRecurseMode recurseMode, ASTVisitor visitor) {
		for (ASTNode node : list) {
			visit(node, recurseMode, visitor);
		}
	}
	
	private void visit(ASTNode node, ASTRecurseMode recurseMode, ASTVisitor visitor) {
		final BaseASTElement element = (BaseASTElement)node;
		
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
