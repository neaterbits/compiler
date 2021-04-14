package com.neaterbits.compiler.bytecode.common.loader.codemap;

import com.neaterbits.compiler.codemap.IntCodeMap;
import com.neaterbits.compiler.codemap.MethodOverrideMap;
import com.neaterbits.compiler.types.MethodVariant;

public final class IntLoaderCodeMap extends IntCodeMap implements LoaderCodeMap {

	private final MethodCallGraph methodCallGraph;

	
	public IntLoaderCodeMap(MethodOverrideMap methodOverrideMap) {
		super(methodOverrideMap);

		this.methodCallGraph = new MethodCallGraph();
	}

	@Override
	public void addMethodCall(int calledFrom, int calledTo) {
		methodCallGraph.addMethodCall(calledFrom, calledTo);
	}

	@Override
	public int recurseCallGraph(int fromMethodNo, MethodRef methodRef) {
		return recurseCallGraph(fromMethodNo, methodRef, 0);
	}

	private int recurseCallGraph(int fromMethodNo, MethodRef methodRef, int depth) {
		
		final int [] calledMethods = methodCallGraph.getMethodsCalledFrom(fromMethodNo);
		
		int numProcessed = 0;
		
		final boolean shouldContinue;
		
		if (depth > 0) {
			final MethodRefStatus status = methodRef.onRef(fromMethodNo, depth);
			
			if (status.isProcessed()) {
				++ numProcessed;
			}
			
			shouldContinue = status.shouldContinue();
		}
		else {
			shouldContinue = true;
		}
		
		if (shouldContinue && calledMethods != null) {
			for (int calledMethod : calledMethods) {
				
				final MethodVariant methodVariant = getMethodVariant(calledMethod);
				
				switch (methodVariant) {
				case STATIC:
				case FINAL_IMPLEMENTATION:
					numProcessed += recurseCallGraph(calledMethod, methodRef, depth + 1);
					break;
					
				case ABSTRACT:
					numProcessed += recurseExtending(calledMethod, methodRef, depth);
					break;
					
				case OVERRIDABLE_IMPLEMENTATION:
					numProcessed += recurseCallGraph(calledMethod, methodRef, depth + 1);
					numProcessed += recurseExtending(calledMethod, methodRef, depth);
					break;
					
				default:
					throw new UnsupportedOperationException();
				}
			}
		}

		return numProcessed;
	}

	private int recurseExtending(int calledMethod, MethodRef methodRef, int depth) {

		final int [] extendingMethods = getMethodsDirectlyExtending(calledMethod);
		
		int numProcessed = 0;
		
		for (int method : extendingMethods) {
			numProcessed += recurseCallGraph(method, methodRef, depth + 1);
		}
	
		return numProcessed;
	}
}
