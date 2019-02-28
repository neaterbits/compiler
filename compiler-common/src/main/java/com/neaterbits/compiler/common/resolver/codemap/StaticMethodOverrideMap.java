package com.neaterbits.compiler.common.resolver.codemap;

import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.subIntArrayInitialIndex;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.subIntArrayLastIndex;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.decodeMethodNo;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.decodeTypeNo;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.encodeMethodWithMethodVariant;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.getTypeVariant;

import com.neaterbits.compiler.common.loader.TypeVariant;

final class StaticMethodOverrideMap extends MethodOverrideMap {

	void addTypeExtendsTypes(int extendingTypeEncoded, int [] extendedTypesEncoded, MethodMap methodMap) {
		
		final int extendingType = decodeTypeNo(extendingTypeEncoded);
		final int [] extendingMethodsEncoded = methodMap.getMethodsForTypeEncoded(extendingType);
		
		for (int i = subIntArrayInitialIndex(extendingMethodsEncoded); i <= subIntArrayLastIndex(extendingMethodsEncoded); ++ i) {
			
			final int extendingMethodEncoded = extendingMethodsEncoded[i];
			
			final int extendingMethod = decodeMethodNo(extendingMethodEncoded);
			
			final int methodSignatureNo = methodMap.getMethodSignature(extendingMethod);
			
			for (int extendedTypeEncoded : extendedTypesEncoded) {
				
				final int extendedType = decodeTypeNo(extendedTypeEncoded);

				final int extendedMethodNo = 
						methodMap.getMethodNoBySignatureNo(extendedType, methodSignatureNo);
				
				if (extendedMethodNo != -1) {
					final TypeVariant extendedTypeVariant = getTypeVariant(extendedTypeEncoded);

					addMethodExtends(
							encodeMethodWithMethodVariant(
									(int)extendedMethodNo,
									extendedTypeVariant),
							
							extendingMethodEncoded);
				}
			}
		}
	}

}
