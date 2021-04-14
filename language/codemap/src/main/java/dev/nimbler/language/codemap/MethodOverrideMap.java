package dev.nimbler.language.codemap;

import static dev.nimbler.language.codemap.ArrayAllocation.addToSubIntArray;
import static dev.nimbler.language.codemap.ArrayAllocation.allocateIntArray;
import static dev.nimbler.language.codemap.ArrayAllocation.subIntArrayInitialIndex;
import static dev.nimbler.language.codemap.ArrayAllocation.subIntArrayLastIndex;
import static dev.nimbler.language.codemap.ArrayAllocation.subIntArraySize;
import static dev.nimbler.language.codemap.ArrayAllocation.subIntArrayValue;
import static dev.nimbler.language.codemap.Encode.decodeMethodNo;
import static dev.nimbler.language.codemap.Encode.getMethodVariant;

import dev.nimbler.language.common.types.MethodVariant;

public abstract class MethodOverrideMap {

    @FunctionalInterface
    interface GetExtendedTypesEncoded {
        int [] getTypes(int type);
    }

	private int [][] extendedMethodsByExtending; // Map from methodNo to an array of methods that are extended by this one
	private int [][] extendingMethodsByExtended;  // Map from methodNo to an array of methods that are extending this one

	abstract void addTypeExtendsTypes(int extendingTypeEncoded, GetExtendedTypesEncoded extendedTypes, MethodMap methodMap);

	private static void checkHasNonStaticMethodVariant(int methodWithMethodVariant) {
		if (getMethodVariant(methodWithMethodVariant) == MethodVariant.STATIC) {
			throw new IllegalArgumentException();
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

		if (getMethodVariant(extendedMethodEncoded) == MethodVariant.FINAL_IMPLEMENTATION) {
		    throw new IllegalArgumentException("Overriding final method");
		}

		checkHasNonStaticMethodVariant(extendedMethodEncoded);
		checkHasNonStaticMethodVariant(extendingMethodEncoded);

		this.extendingMethodsByExtended = allocateIntArray(this.extendingMethodsByExtended, extendedMethod + 1, false);
		addToSubIntArray(extendingMethodsByExtended, extendedMethod, extendingMethodEncoded, 3);

		this.extendedMethodsByExtending = allocateIntArray(this.extendedMethodsByExtending, extendingMethod + 1, false);
		addToSubIntArray(extendedMethodsByExtending, extendingMethod, extendedMethodEncoded, 3);
	}

	private static boolean hasMethod(int [][] array, int methodNo) {

        final boolean hasMethod;

        if (array == null) {
            hasMethod = false;
        }
        else if (methodNo > array.length) {
            hasMethod = false;
        }
        else if (array[methodNo] == null) {
            hasMethod = false;
        }
        else {
            hasMethod = true;
        }

        return hasMethod;
	}

	private static int getNum(int [][] array, int methodNo) {

	    return hasMethod(array, methodNo)
	            ? subIntArraySize(array, methodNo)
                : 0;
	}

    private static int [] getMethods(int [][] array, int methodNo) {

        return hasMethod(array, methodNo)
                ? decodeMethods(array[methodNo])
                : null;
    }

	final int getNumberOfMethodsDirectlyExtending(int methodNo) {

		return getNum(this.extendingMethodsByExtended, methodNo);
	}

	final int [] getMethodsDirectlyExtending(int methodNo) {

		return getMethods(this.extendingMethodsByExtended, methodNo);
	}


	final int getNumberOfMethodsDirectlyExtendedBy(int methodNo) {

		return getNum(this.extendedMethodsByExtending, methodNo);
	}

	final int [] getMethodsDirectlyExtendedBy(int methodNo) {

		return getMethods(this.extendedMethodsByExtending, methodNo);
	}

	private static int [] decodeMethods(int [] array) {

	    final int initial = subIntArrayInitialIndex(array);
	    final int last = subIntArrayLastIndex(array);

	    final int [] dst = new int[subIntArraySize(array)];

	    int dstIdx = 0;

	    for (int i = initial; i <= last; ++ i) {
	        dst[dstIdx ++] = Encode.decodeMethodNo(array[i]);
	    }

	    return dst;
	}

	final int getTotalNumberOfMethodsExtending(int methodNo) {

		int count = 0;

		if (hasMethod(this.extendingMethodsByExtended, methodNo)) {

			final int subSize = subIntArraySize(extendingMethodsByExtended, methodNo);

			count += subSize;

			for (int i = 0; i < subSize; ++ i) {

			    final int extendingMethodEncoded = subIntArrayValue(extendingMethodsByExtended[methodNo], i);

				count += getTotalNumberOfMethodsExtending(Encode.decodeMethodNo(extendingMethodEncoded));
			}
		}

		return count;
	}
}
