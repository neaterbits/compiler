package com.neaterbits.compiler.codemap;

import com.neaterbits.compiler.types.FieldInfo;
import com.neaterbits.compiler.types.MethodInfo;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.TypeVariant;
import com.neaterbits.compiler.types.Visibility;

public interface CodeMap {

	public static class TypeResult {
		public int type;
	}

	public enum MethodRefStatus {
		PROCESSED_CONTINUE(true, true),
		SKIPPED_CONTINUE(false, true),

		PROCESSED_BREAK(true, false),
		SKIPPED_BREAK(false, false);

		private final boolean processed;
		private final boolean shouldContinue;

		private MethodRefStatus(boolean processed, boolean shouldContinue) {
			this.processed = processed;
			this.shouldContinue = shouldContinue;
		}

		public boolean isProcessed() {
			return processed;
		}

		public boolean shouldContinue() {
			return shouldContinue;
		}
	}

	@FunctionalInterface
	interface MethodRef {
		MethodRefStatus onRef(int methodNo, int depth);
	}

	default int addType(TypeVariant typeVariant) {
	    return addType(typeVariant, null, null);
	}

	int addType(TypeVariant typeVariant, int [] extendsFromClasses, int [] extendsFromInterfaces);

	TypeVariant getTypeVariantForType(int typeNo);

	int getExtendsFromSingleSuperClass(int typeNo);

	int [] getTypesThisDirectlyExtends(int typeNo);

	int [] getTypesDirectlyExtendingThis(int typeNo);

	int [] getAllTypesExtendingThis(int typeNo);

	int addField(
			int type,
			String name,
			int fieldType,
			boolean isStatic,
			Visibility visibility,
			Mutability mutability,
			boolean isVolatile,
			boolean isTransient,
			int indexInType);

	FieldInfo getFieldInfo(int typeNo, String fieldName);

	void setMethodCount(int type, int methodCount);

	int addOrGetMethod(
			int type,
			String methodName,
			MethodVariant methodVariant,
			int returnType,
			int [] parameters,
			int indexInType);

	int addOrGetExtendingMethod(
			int extendedMethod, MethodVariant extendedMethodVariant,
			int type,
			String methodName,
			MethodVariant methodVariant,
			int returnType,
			int [] parameters,
			int indexInType);


	void computeMethodExtends(int typeNo);

	int getTypeForMethod(int methodNo);

	int getIndexForMethod(int methodNo);

	MethodInfo getMethodInfo(int typeNo, String methodName, int [] parameterTypes);

	@FunctionalInterface
	interface MethodFilter {
		boolean addMethod(int methodNo, MethodVariant methodVariant);
	}

	int getDistinctMethodCount(int typeNo, MethodFilter methodFilter, VTableScratchArea scratchArea);
}
