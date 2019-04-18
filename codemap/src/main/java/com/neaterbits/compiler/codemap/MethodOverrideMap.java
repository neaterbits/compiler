package com.neaterbits.compiler.codemap;

import static com.neaterbits.compiler.codemap.ArrayAllocation.addToSubIntArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateIntArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.subIntArrayCopy;
import static com.neaterbits.compiler.codemap.ArrayAllocation.subIntArraySize;
import static com.neaterbits.compiler.codemap.Encode.decodeMethodNo;
import static com.neaterbits.compiler.codemap.Encode.getMethodVariant;

public abstract class MethodOverrideMap {
	
	private int [][] extendedMethodsByExtending; // Map from methodNo to an array of methods that are extended by this one
	private int [][] extendingMethodsByExtended;  // Map from methodNo to an array of methods that are extending this one

	abstract void addTypeExtendsTypes(int extendingTypeEncoded, int [] extendedTypesEncoded, MethodMap methodMap);
	
	private static void checkHasNonStaticMethodVariant(int methodWithMethodVariant) {
		if (getMethodVariant(methodWithMethodVariant) == MethodVariant.STATIC) {
			throw new IllegalStateException();
		}
	}

	final void addMethodExtends(
			int extendedMethod, MethodVariant extendedMethodVariant,
			int extendingMethod, MethodVariant extendingMethodVariant) {
		
		addMethodExtends(
				Encode.encodeMethodWithoutTypeVariant(extendedMethod, extendedMethodVariant),
				Encode.encodeMethodWithoutTypeVariant(extendingMethod, extendingMethodVariant));
	}

	// Add a method directly overriding another one, in a class or interface
	final void addMethodExtends(int extendedMethodEncoded, int extendingMethodEncoded) {

		final int extendedMethod  = decodeMethodNo(extendedMethodEncoded);
		final int extendingMethod = decodeMethodNo(extendingMethodEncoded);

		checkHasNonStaticMethodVariant(extendedMethodEncoded);
		checkHasNonStaticMethodVariant(extendingMethodEncoded);

		this.extendingMethodsByExtended = allocateIntArray(this.extendingMethodsByExtended, extendedMethod + 1, false);
		addToSubIntArray(extendingMethodsByExtended, extendedMethod, extendingMethodEncoded, 3);

		this.extendedMethodsByExtending = allocateIntArray(this.extendedMethodsByExtending, extendingMethod + 1, false);
		addToSubIntArray(extendedMethodsByExtending, extendingMethod, extendedMethodEncoded, 3);
	}

	final int getNumberOfMethodsDirectlyExtending(int methodNo) {
		return this.extendingMethodsByExtended[methodNo] != null ? subIntArraySize(this.extendingMethodsByExtended, methodNo) : 0;
	}
	
	final int [] getMethodsDirectlyExtending(int methodNo) {
		return this.extendingMethodsByExtended[methodNo] != null ? subIntArrayCopy(this.extendingMethodsByExtended[methodNo]) : null;
	}
	

	final int getNumberOfMethodsDirectlyExtendedBy(int methodNo) {
		return this.extendedMethodsByExtending[methodNo] != null ? subIntArraySize(this.extendedMethodsByExtending, methodNo) : 0;
	}

	final int [] getMethodsDirectlyExtendedBy(int methodNo) {
		
		return this.extendedMethodsByExtending[methodNo] != null ? subIntArrayCopy(this.extendedMethodsByExtending[methodNo]) : null;
	}
	
	final int getTotalNumberOfMethodsExtending(int methodNo) {

		int count = 0;
		
		if (this.extendedMethodsByExtending[methodNo] != null) {
			final int subSize = subIntArraySize(extendedMethodsByExtending, methodNo);

			for (int i = 0; i < subSize; ++ i) {
				count += getTotalNumberOfMethodsExtending(extendedMethodsByExtending[methodNo][i + 1]);
			}
		}
		
		return count;
	}
}
