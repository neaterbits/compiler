package com.neaterbits.compiler.common.resolver.references;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.loader.TypeVariant;

final class MethodMap extends BaseReferences {

	private int methodNo;
	
	private int parameterSignatureNo;
	
	// Each unique signature is given a sequence number that is used as index here so that
	// we can reuse distinct signatures
	private BaseType [][] parameterSignatures;
	
	private int methodNameNo;
	
	private String [] methodNames;
	
	// Unique method signatures, 32 bits for name and 32 bits for parameters
	private long [] methodSignatures; 

	private int [][] extendedBy;
	private int [][] extending;
	
	private int [][] methodsByType;
	
	
	int addMethod(int typeNoEncoded, String name, BaseType [] parameters, MethodMapCache cache) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(parameters);
		
		final TypeVariant typeVariant = getTypeVariant(typeNoEncoded);
		
		Integer paramsIndex = cache.getParamsIndex(parameters);
		
		if (paramsIndex == null) {
			
			this.parameterSignatures = allocateArray(parameterSignatures, parameterSignatureNo + 1, length -> new BaseType[length][]);

			paramsIndex = parameterSignatureNo ++;

			this.parameterSignatures[paramsIndex] = parameters;
		}
		
		Integer nameIndex = cache.getNameIndex(name);
		
		if (nameIndex == null) {
			this.methodNames = allocateArray(methodNames, methodNameNo + 1, length -> new String[length]);
			
			nameIndex = methodNameNo ++;
			
			this.methodNames[nameIndex] = name;
		}

		this.methodSignatures = allocateLongArray(methodSignatures, methodNo + 1);
		
		final int methodIndex = methodNo ++;
		
		methodSignatures[methodIndex] = nameIndex.longValue() << 32 | paramsIndex.longValue(); 
	
		this.methodsByType = allocateIntArray(this.methodsByType, typeNoEncoded + 1, false);
		
		addToSubIntArray(methodsByType, typeNoEncoded, methodNo, 5);
		
		this.extendedBy = allocateIntArray(extendedBy, methodNo);
		this.extending = allocateIntArray(extending, methodNo);
		
		return encodeType(methodIndex, typeVariant);
	}
	
	// Add a method directly overriding another one, in a class or interface
	void addMethodExtends(int extendedMethodEncoded, int extendingMethodEncoded) {
		addToSubIntArray(extendedBy, extendedMethodEncoded, extendingMethodEncoded, 3);
		addToSubIntArray(extending, extendingMethodEncoded, extendedMethodEncoded, 3);
	}
	
	int getNumberOfMethodsDirectlyExtending(int encodedMethodNo) {
		
		final int method = decodeMethodNo(encodedMethodNo);
		
		return this.extendedBy[method] != null ? subIntArraySize(this.extendedBy, method) : 0;
	}

	int getTotalNumberOfMethodsExtending(int encodedMethodNo) {

		final int method = decodeMethodNo(encodedMethodNo);
	
		int count = 0;
		
		if (this.extendedBy[method] != null) {
			final int subSize = subIntArraySize(extendedBy, method);

			for (int i = 0; i < subSize; ++ i) {
				count += getTotalNumberOfMethodsExtending(extendedBy[method][i + 1]);
			}
		}
		
		return count;
	}
	
	
	// Utilized while building integer maps, reusing same index for same names or signatures
	static class MethodMapCache {
		private final Map<Parameters, Integer> paramsToIndex;
		private final Map<String, Integer> nameToIndex;
		
		MethodMapCache() {
			this.paramsToIndex = new HashMap<>();
			this.nameToIndex = new HashMap<>();
		}
		
		Integer getParamsIndex(BaseType [] params) {
			return paramsToIndex.get(new Parameters(params));
		}
		
		void addParamsIndex(BaseType [] params, int index) {
			paramsToIndex.put(new Parameters(params), index);
		}
		
		Integer getNameIndex(String name) {
			return nameToIndex.get(name);
		}
		
		void addNameIndex(String name, int index) {
			nameToIndex.put(name, index);
		}
	}
	
	private static class Parameters {
		private final BaseType [] params;

		Parameters(BaseType[] params) {
			this.params = params;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(params);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Parameters other = (Parameters) obj;
			if (!Arrays.equals(params, other.params))
				return false;
			return true;
		}
	}
	
}
