package dev.nimbler.language.codemap;

import static dev.nimbler.language.codemap.ArrayAllocation.subIntArrayInitialIndex;
import static dev.nimbler.language.codemap.ArrayAllocation.subIntArrayLastIndex;
import static dev.nimbler.language.codemap.Encode.decodeMethodNo;
import static dev.nimbler.language.codemap.Encode.decodeTypeNo;
import static dev.nimbler.language.codemap.Encode.encodeMethodWithMethodVariant;
import static dev.nimbler.language.codemap.Encode.getTypeVariant;

import dev.nimbler.language.common.types.TypeVariant;

public final class StaticMethodOverrideMap extends MethodOverrideMap {

	@Override
	void addTypeExtendsTypes(int extendingTypeEncoded, GetExtendedTypesEncoded getExtendedTypesEncoded, MethodMap methodMap) {

		final int extendingType = decodeTypeNo(extendingTypeEncoded);
		final int [] extendingMethodsEncoded = methodMap.getMethodsForTypeEncoded(extendingType);

		final int initial = subIntArrayInitialIndex(extendingMethodsEncoded);
		final int last = subIntArrayLastIndex(extendingMethodsEncoded);

		for (int i = initial; i <= last; ++ i) {

			final int extendingMethodEncoded = extendingMethodsEncoded[i];

			final int extendingMethod = decodeMethodNo(extendingMethodEncoded);

			final int methodSignatureNo = methodMap.getMethodSignature(extendingMethod);

			addFromExtendedTypes(
			        extendingMethodEncoded,
			        methodSignatureNo,
			        extendingType,
			        getExtendedTypesEncoded,
			        methodMap);
		}
	}

	private boolean addFromExtendedTypes(
	        int extendingMethodEncoded,
	        int methodSignatureNo,
	        int typeNo,
	        GetExtendedTypesEncoded getExtendedTypesEncoded,
	        MethodMap methodMap) {

	    final int [] extendedTypesEncoded = getExtendedTypesEncoded.getTypes(typeNo);

	    boolean added = false;

	    if (extendedTypesEncoded != null) {

	        for (int extendedTypeEncoded : extendedTypesEncoded) {

                final int extendedType = decodeTypeNo(extendedTypeEncoded);

                final int extendedMethodNo =
                        methodMap.getMethodNoBySignatureNo(extendedType, methodSignatureNo);

                if (extendedMethodNo != -1) {
                    final TypeVariant extendedTypeVariant = getTypeVariant(extendedTypeEncoded);

                    addMethodExtends(
                            encodeMethodWithMethodVariant(
                                    extendedMethodNo,
                                    extendedTypeVariant),

                            extendingMethodEncoded);

                    added = true;
                    break;
                }
                else {

                    added = addFromExtendedTypes(
                            extendingMethodEncoded,
                            methodSignatureNo,
                            extendedType,
                            getExtendedTypesEncoded,
                            methodMap);

                    if (added) {
                        break;
                    }
                }
            }
	    }

	    return added;
	}
}
