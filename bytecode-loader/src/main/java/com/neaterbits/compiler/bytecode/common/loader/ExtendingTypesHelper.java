package com.neaterbits.compiler.bytecode.common.loader;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.common.FieldType;
import com.neaterbits.compiler.common.resolver.codemap.MethodVariant;

class ExtendingTypesHelper {

	static void addAnyExtendingMethods(
			ClassBytecode classBytecode,
			int type,
			int methodNo, int requestedMethodIdx,
			NameLoadMethodRequest request,
			LoaderMaps loaderMaps) {
		
		final MethodVariant methodVariant = classBytecode.getMethodVariant(requestedMethodIdx);
		
		switch (methodVariant) {
		case FINAL_IMPLEMENTATION:
		case STATIC:
			break;
			
		case ABSTRACT:
		case OVERRIDABLE_IMPLEMENTATION:
			// Add any not yet added overriding methods from loaded classes
			ExtendingTypesHelper.addMethodForExtendingTypes(
					type,
					methodNo, methodVariant,
					request,
					loaderMaps);
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
	}
	

	private static void addMethodForExtendingTypes(
			int type,
			int extendedMethod, MethodVariant extendedMethodVariant,
			NameLoadMethodRequest request,
			LoaderMaps loaderMaps) {
		
		final int [] extendingTypes = loaderMaps.codeMap.getDirectlyExtendingTypes(type);
		
		for (int extendingType : extendingTypes) {
			final ClassBytecode classBytecode = loaderMaps.loadedClasses.getBytecode(extendingType);
			
			if (classBytecode == null) {
				throw new IllegalStateException();
			}
			
			final int methodIdx = classBytecode.getMethodIndex(
					request.getMethodName(),
					request.getParameterTypes());
			
			if (methodIdx >= 0) {
				
				final FieldType returnTypeName = classBytecode.getMethodReturnType(methodIdx);
				
				final MethodVariant extendingMethodVariant = classBytecode.getMethodVariant(methodIdx);
				
				extendedMethod = loaderMaps.codeMap.addOrGetExtendingMethod(
						extendedMethod, extendedMethodVariant,
						extendingType,
						request.getMethodName(),
						extendingMethodVariant,
						loaderMaps.getReturnType(returnTypeName),
						loaderMaps.getParameters(request.getParameterTypes()),
						methodIdx);
			
				extendedMethodVariant = extendingMethodVariant;
			}
			
			addMethodForExtendingTypes(extendingType, extendedMethod, extendedMethodVariant, request, loaderMaps);
		}
	}
}
