package com.neaterbits.compiler.common.resolver.codemap;

import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.util.Bits;
import com.neaterbits.compiler.common.util.ValueMap;

public final class IntCodeMap implements CodeMap {

	private static final int NUM_METHOD_VARIANT_BITS = Bits.getNumBitsForStoringMaxValue(MethodVariant.values().length);
	
	private final TypeHierarchy typeHierarchy;

	private final MethodMap methodMap;
	private final MethodMapCache methodMapCache;
	
	private final DynamicMethodOverrideMap methodOverrideMap;
	
	private final ValueMap methodVariants;
	private final MethodCallGraph methodCallGraph;
	
	public IntCodeMap() {
		this.typeHierarchy 	= new TypeHierarchy();
		
		this.methodMap = new MethodMap();
		this.methodMapCache = new MethodMapCache();
		
		this.methodOverrideMap = new DynamicMethodOverrideMap();
		
		this.methodVariants = new ValueMap(NUM_METHOD_VARIANT_BITS, 10000);
		this.methodCallGraph = new MethodCallGraph();
	}

	@Override
	public int addType(TypeVariant typeVariant, int[] extendsFrom) {

		final int [] extendsFromEncoded;
		
		if (extendsFrom != null) {
			extendsFromEncoded = new int[extendsFrom.length];
			
			for (int i = 0; i < extendsFromEncoded.length; ++ i) {
				final int extendsFromType = extendsFrom[i];
				
				extendsFromEncoded[i] = Encode.encodeType(
						extendsFromType,
						typeHierarchy.getTypeVariantForType(extendsFromType));
			}
		}
		else {
			extendsFromEncoded = null;
		}
		
		return typeHierarchy.addType(typeVariant, extendsFromEncoded);
	}

	@Override
	public int getExtendsFromSingleSuperClass(int type) {
		return typeHierarchy.getExtendsFromSingleSuperClass(type);
	}

	@Override
	public void setMethodCount(int type, int methodCount) {
		methodMap.allocateMethods(type, methodCount);
	}

	@Override
	public int addOrGetMethod(int type, String methodName, MethodVariant methodVariant, int returnType, int [] parameters, int indexInType) {
		
		final int methodNo = methodMap.addMethod(
				type,
				typeHierarchy.getTypeVariantForType(type),
				methodName,
				parameters,
				methodVariant,
				indexInType,
				methodMapCache);
		
		methodVariants.storeValue(methodNo, methodVariant.ordinal());
		
		return methodNo;
	}

	@Override
	public int addOrGetExtendingMethod(
			int extendedMethod, MethodVariant extendedMethodVariant,
			int type,
			String methodName,
			MethodVariant methodVariant,
			int returnType,
			int[] parameters,
			int indexInType) {

		int methodNo = methodMap.getMethodNo(type, methodName, parameters, methodMapCache);
		
		if (methodNo < 0) {
			methodNo = addOrGetMethod(type, methodName, methodVariant, returnType, parameters, indexInType);
		}
		
		methodOverrideMap.addMethodExtends(
				extendedMethod, extendedMethodVariant,
				methodNo, methodVariant);
		
		return methodNo;
	}

	private MethodVariant getMethodVariant(int methodNo) {
		final long value = methodVariants.getValue(methodNo);
		
		return MethodVariant.values()[(int)value];
	}
	
	@Override
	public int[] getDirectlyExtendingTypes(int type) {
		return typeHierarchy.getExtendsFrom(type, Encode::isClass);
	}

	@Override
	public int getTypeForMethod(int methodNo) {
		return methodMap.getTypeForMethod(methodNo);
	}

	@Override
	public int getIndexForMethod(int methodNo) {
		return methodMap.getIndexForMethod(methodNo);
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
	
	@Override
	public int getDistinctMethodCount(int typeNo, MethodFilter methodFilter, VTableScratchArea scratchArea) {
		return methodMap.getDistinctMethodCount(typeNo, methodFilter, typeHierarchy::getExtendsFromSingleSuperClass, scratchArea);
	}

	private int recurseExtending(int calledMethod, MethodRef methodRef, int depth) {

		final int [] extendingMethods = methodOverrideMap.getMethodsDirectlyExtending(calledMethod);
		
		int numProcessed = 0;
		
		for (int method : extendingMethods) {
			numProcessed += recurseCallGraph(method, methodRef, depth + 1);
		}
	
		return numProcessed;
	}
}
