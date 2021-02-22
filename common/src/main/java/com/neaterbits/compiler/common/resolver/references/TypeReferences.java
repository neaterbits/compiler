package com.neaterbits.compiler.common.resolver.references;

import com.neaterbits.compiler.common.loader.ResolvedType;

final class TypeReferences<TYPE extends ResolvedType> extends BaseReferences {

	private int[][] fieldReferences;
	private int[][] methodReferences;

	void addFieldReferences(int fromType, int ... toTypes) {
		
		this.fieldReferences = allocateIntArray(this.fieldReferences, fromType + 1);
		
		if (this.fieldReferences[fromType] != null) {
			throw new IllegalStateException("Field references already set");
		}
		
		this.fieldReferences[fromType] = toTypes;
	}
	
	void addMethodReferences(int fromType, int ... toTypes) {
		
		this.methodReferences = allocateIntArray(this.methodReferences, fromType + 1);
		
		if (this.methodReferences[fromType] != null) {
			throw new IllegalStateException("Method references already set");
		}
		
		this.methodReferences[fromType] = toTypes;
	}

	int [] getFieldReferences(int fromType) {
		return fieldReferences[fromType];
	}

	int [] getMethodReferences(int fromType) {
		return methodReferences[fromType];
	}
}
