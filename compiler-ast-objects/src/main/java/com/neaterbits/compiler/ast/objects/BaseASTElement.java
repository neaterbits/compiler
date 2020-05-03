package com.neaterbits.compiler.ast.objects;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTNode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.Stack;
import com.neaterbits.compiler.util.StackView;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public abstract class BaseASTElement extends ASTNode {
	
    public static boolean REQUIRE_CONTEXT = true;
    
	protected static final boolean CONTINUE_ITERATION = true;
	
	private final Context context;

	public abstract ParseTreeElement getParseTreeElement();
	
	public BaseASTElement(Context context) {
		
		if (REQUIRE_CONTEXT && !isPlaceholderElement()) {
			Objects.requireNonNull(context);
		}
		
		this.context = context;
	}

	public final Context getContext() {
		return context;
	}

	public final boolean isPlaceholderElement() {
		return this instanceof BasePlaceholderASTElement;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
	protected abstract void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator);

	public final void iterateNodeFirst(ASTVisitor visitor) {
		
		final BaseASTIterator iterator = new BaseASTIterator() {
			@Override
			public boolean onElement(BaseASTElement element) {
				visitor.onElement(element);
				
				return true;
			}
		};
		
		iterateNodeFirst(iterator);
	}
	
	private static class FoundElement {
		private BaseASTElement found;
		
		private boolean onlyLeavesElementFound;
	}
	
	public final BaseASTElement findElement(boolean onlyLeaves, Predicate<BaseASTElement> test) {

		final FoundElement foundElement = new FoundElement();
		
		final BaseASTIterator iterator = new BaseASTIterator() {
			
			@Override
			public void onPush(BaseASTElement element) {
				
				super.onPush(element);

			}

			@Override
			public boolean onElement(BaseASTElement element) {

				final boolean continueIteration;
				
				if (test.test(element)) {
					foundElement.found = element;
					
					if (onlyLeaves) {
						// must continue iteration to check for whether are subnodes
						continueIteration = true;
					}
					else {
						// non-leaves OK, exit iteration
						continueIteration = false;
					}
				}
				else {
					// test returned false, continue searching
					continueIteration = true;
				}

				return continueIteration;
			}

			@Override
			public boolean onPop(BaseASTElement element) {
				super.onPop(element);
				
				final boolean continueIteration;
				
				if (onlyLeaves) {
					if (foundElement.found != null) {
						if (foundElement.found != element) {
							
							// only reset if not decidedly found, so that not reset by parent nodes
							if (!foundElement.onlyLeavesElementFound) {
								// found sub-element so reset found
								foundElement.found = null;
							}
							
							continueIteration = true;
						}
						else {
							// pop found element without being reset in the meantime
							foundElement.onlyLeavesElementFound = true;
							continueIteration = false;
						}
					}
					else {
						// no element found, continue iteration
						continueIteration = true;
					}
				}
				else {
					// not only leaves, continue iteration unless onElement() returned false
					continueIteration = true;
				}
				
				return continueIteration;
			}
		};
		
		iterateNodeFirst(iterator);

		return foundElement.found;
		
	}

	private void iterateNodeFirst(ASTIterator iterator) {

		iterator.onElement(this);
		
		doRecurse(ASTRecurseMode.VISIT_NODE_FIRST, iterator);
	}
	
	public final void iterateNodeLast(ASTVisitor visitor) {

		final BaseASTIterator iterator = new BaseASTIterator() {
			@Override
			public boolean onElement(BaseASTElement element) {
				visitor.onElement(element);
				
				return true;
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

	private static class ASTVisitorForStack<T, STACKVIEW extends StackView<T>, STACK extends Stack<T>> extends BaseASTIterator {
		
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
		public boolean onElement(BaseASTElement element) {
			stackVisitor.onElement(element, stackView);
			
			return true;
		}

		@Override
		public boolean onPop(BaseASTElement element) {
			stack.pop();
			
			return true;
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

		final BaseASTIterator baseASTIterator = (BaseASTIterator)iterator;

		if (baseASTIterator.isContinueIteration()) {
	
			final BaseASTElement element = (BaseASTElement)single.get();
	
			iterator.onPush(element);
			
			final boolean continueIterationOnVisit = visit(element, recurseMode, iterator);
	
			final boolean continueIterationOnPop = iterator.onPop(element);

			if (!continueIterationOnVisit || !continueIterationOnPop) {
				baseASTIterator.cancelIteration();
			}
		}
	}

	protected final void doIterate(ASTList<? extends ASTNode> list, ASTRecurseMode recurseMode, ASTIterator iterator) {

		for (ASTNode node : list) {
			
			final BaseASTIterator baseASTIterator = (BaseASTIterator)iterator;
			
			if (!baseASTIterator.isContinueIteration()) {
				break;
			}
			
			final BaseASTElement element = (BaseASTElement)node;

			iterator.onPush(element);
			
			final boolean continueIterationOnVisit = visit(element, recurseMode, iterator);

			final boolean continueIterationOnPop = iterator.onPop(element);
			
			if (!continueIterationOnVisit || !continueIterationOnPop) {
				baseASTIterator.cancelIteration();
				break;
			}
		}
	}
	
	private boolean visit(BaseASTElement element, ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		final boolean continueIteration;
		
		switch (recurseMode) {
		case VISIT_NODE_FIRST:
			continueIteration = iterator.onElement(element);
			
			if (continueIteration) {
			
				if (element.isInList()) {
					element.doRecurse(recurseMode, iterator);
				}
			}
			break;
			
		case VISIT_NODE_LAST:
			element.doRecurse(recurseMode, iterator);

			continueIteration = iterator.onElement(element);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown recurse mode " + recurseMode);
		}
		
		return continueIteration;
	}
}
