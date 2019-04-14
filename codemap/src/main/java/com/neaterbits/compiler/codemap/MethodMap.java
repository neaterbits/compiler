package com.neaterbits.compiler.codemap;

import static com.neaterbits.compiler.codemap.ArrayAllocation.addToSubIntArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateIntArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateLongArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateSubArray;
import static com.neaterbits.compiler.codemap.Encode.decodeMethodNo;
import static com.neaterbits.compiler.codemap.Encode.encodeMethod;
import static com.neaterbits.compiler.codemap.Encode.encodeMethodWithoutTypeVariant;
import static com.neaterbits.compiler.codemap.Encode.encodeType;
import static com.neaterbits.compiler.codemap.Encode.getMethodVariant;
import static com.neaterbits.compiler.codemap.Hash.GetCompareValue;

import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.compiler.codemap.CodeMap.MethodFilter;


final class MethodMap {

	private static final int NO_PARAM_TYPES_INDEX = 0;
	
	// Sequence number for methods
	// Each method has their own methodNo even if same signature
	// (eg. multiple implementations of same interface)
	private int methodNo;
	
	// Each unique signature set of types is given a sequence number that is used as index here so that
	// we can reuse distinct signatures
	private int parameterSignatureNo;
	private int [][] parameterSignatures;
	
	// Each unique method name is given a sequence number
	private int methodNameNo;
	private String [] methodNames;
	
	// Each unique name and type combination is given a sequence number
	private int methodSignatureNo;
	// Unique method signatures, 32 bits for name and 32 bits for parameters
	private long [] methodSignaturesBySignatureIndex;
	
	private int [][] methodsByType;

	private int [] typeByMethod;		// Find type from methodNo
	private int [] indexByMethod;		// Index into type by methodNo
	private int [] methodSignaturesByMethod; // Find unique signature from methodNo
	
	// hashmap as simple long [] array
	// for finding methodno for a particular type/method signature combination
	// entries are 20 bits for typeno, 20 bits for signatureno and 24 bits for methodno
	private long [] typeAndMethodSignatureToMethodHash;

	private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;
	
	MethodMap() {
		this.typeAndMethodSignatureToMethodHash = Hash.makeHashMap(10000, HASH_UNDEF);
		
		this.parameterSignatureNo = NO_PARAM_TYPES_INDEX + 1;
	}
	
	
	private static final GetCompareValue TYPE_AND_METHOD_SIGNATURE_HASH = new GetCompareValue() {
		
		private static final int VALUE_BITS = Encode.METHOD_AND_VARIANT_BITS;
		
		@Override
		public long makeMapValue(long key, long value) {
			
			if (value >= (1 << VALUE_BITS)) {
				throw new IllegalArgumentException(String.format("value: %x / %x", value, (1 << VALUE_BITS)));
			}
			
			return key << VALUE_BITS | value;
		}
		
		@Override
		public long getValue(long mapValue) {
			return mapValue & ((1 << VALUE_BITS) - 1);
		}
		
		@Override
		public long getKey(long mapValue) {
			return mapValue >>> VALUE_BITS;
		}
		
		@Override
		public long getDefaultValue() {
			return HASH_UNDEF;
		}
	}; 
	
	private static long typeAndSignatureKey(int typeNo, int signatureNo) {
		return typeNo << Encode.SIGNATURE_BITS | signatureNo;
	}
	
	private void addMappingFromTypeAndSignatureToMethod(int typeNo, int signatureNo, int methodNoEncodedWithoutTypeVariant) {
		
		final long key = typeAndSignatureKey(typeNo, signatureNo);
		
		this.typeAndMethodSignatureToMethodHash = Hash.hashStore(typeAndMethodSignatureToMethodHash, key, methodNoEncodedWithoutTypeVariant, HASH_UNDEF, TYPE_AND_METHOD_SIGNATURE_HASH);
	}

	private int findOrAddSignature(String name, int [] parameters, MethodMapCache cache) {
		Integer paramsIndex = cache.getParamsIndex(parameters);
		
		if (paramsIndex == null) {
			
			this.parameterSignatures = allocateIntArray(parameterSignatures, parameterSignatureNo + 1);

			paramsIndex = parameterSignatureNo ++;

			this.parameterSignatures[paramsIndex] = parameters;
			
			cache.addParamsIndex(parameters, paramsIndex);
		}
		
		Integer nameIndex = cache.getNameIndex(name);
		
		if (nameIndex == null) {
			this.methodNames = allocateArray(methodNames, methodNameNo + 1, length -> new String[length]);
			
			nameIndex = methodNameNo ++;
			
			this.methodNames[nameIndex] = name;
			
			cache.addNameIndex(name, nameIndex);
		}
		
		int methodSignatureIndex = cache.getSignatureNo(nameIndex, paramsIndex);
		
		if (methodSignatureIndex < 0) {
			methodSignatureIndex = methodSignatureNo ++;
			
			this.methodSignaturesBySignatureIndex = allocateLongArray(methodSignaturesBySignatureIndex, methodSignatureIndex + 1);
			
			methodSignaturesBySignatureIndex[methodSignatureIndex] = nameIndex.longValue() << 32 | paramsIndex.longValue();
			
			cache.storeSignatureNo(nameIndex, paramsIndex, methodSignatureIndex);
		}

		return methodSignatureIndex;
	}
	
	void allocateMethods(int typeNo, int numMethods) {
		this.methodsByType = allocateIntArray(methodsByType, typeNo + 1);
		
		this.methodsByType[typeNo] = allocateSubArray(numMethods);
	}
	
	int addMethod(int typeNo, TypeVariant typeVariant, String name, int [] parameters, MethodVariant methodVariant, int indexInType, MethodMapCache cache) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(parameters);
		
		// Allocate new method identifier
		final int methodIndex = methodNo ++;
		final int numMethods = methodNo;

		final int signatureNo = findOrAddSignature(name, parameters, cache);
		
		this.methodSignaturesByMethod = allocateIntArray(methodSignaturesByMethod, numMethods);
		methodSignaturesByMethod[methodIndex] = signatureNo;

		final int typeNoEncoded = encodeType(typeNo, typeVariant);
		
		final int methodNoEncodedWithoutTypeVariant = encodeMethodWithoutTypeVariant(methodIndex, methodVariant);
		addMappingFromTypeAndSignatureToMethod(typeNo, signatureNo, methodNoEncodedWithoutTypeVariant);

		final int encodedMethod = encodeMethod(methodIndex, typeVariant, methodVariant);
		
		this.methodsByType = allocateIntArray(this.methodsByType, typeNo + 1, false);
		addToSubIntArray(methodsByType, typeNo, encodedMethod, 5);
		
		this.typeByMethod = allocateIntArray(this.typeByMethod, numMethods);
		this.typeByMethod[methodIndex] = typeNoEncoded;

		this.indexByMethod = allocateIntArray(this.indexByMethod, numMethods);
		this.indexByMethod[methodIndex] = indexInType;

		return methodIndex;
	}
	
	
	MethodInfo getMethodInfo(int typeNo, String methodName, int [] parameterTypes, MethodMapCache methodMapCache) {

		final Integer nameIndex = methodMapCache.getNameIndex(methodName);

		final MethodInfo methodInfo;

		if (nameIndex == null) {
			methodInfo = null;
		}
		else {
			
			final Integer paramIndex;
			
			if (parameterTypes == null || parameterTypes.length == 0) {
				paramIndex = NO_PARAM_TYPES_INDEX;
			}
			else {
				paramIndex = methodMapCache.getParamsIndex(parameterTypes);
			}

			if (paramIndex == null) {
				methodInfo = null;
			}
			else {
				final long signatureNo = methodMapCache.getSignatureNo(nameIndex, paramIndex);
				
				final long key = typeAndSignatureKey(typeNo, (int)signatureNo);
				
				final long method = Hash.hashGet(typeAndMethodSignatureToMethodHash, key, HASH_UNDEF, TYPE_AND_METHOD_SIGNATURE_HASH);
				
				if (method == HASH_UNDEF) {
					methodInfo = null;
				}
				else {
					final int methodEncoded = (int)method;
					
					methodInfo = new MethodInfo(decodeMethodNo(methodEncoded), getMethodVariant(methodEncoded));
				}
			}
			
			return methodInfo;
		}
		
		return methodInfo;
	}
	
	int getMethodNo(int typeNo, String methodName, int [] parameterTypes, MethodMapCache methodMapCache) {

		final MethodInfo methodInfo = getMethodInfo(typeNo, methodName, parameterTypes, methodMapCache);
		
		return methodInfo != null ? methodInfo.getMethodNo() : -1;
	}
	
	String getMethodName(int methodNo) {
		return methodNames[(int)(methodSignaturesBySignatureIndex[methodNo] >> 32)];
	}
	
	int [] getMethodParameterTypes(int methodNo) {
		
		final int signatureIndex = methodSignaturesByMethod[methodNo];

		final int [] types = parameterSignatures[(int)(methodSignaturesBySignatureIndex[signatureIndex] & 0xFFFFFFFF)];

		return types != null ? Arrays.copyOf(types, types.length) : null;
	}
	
	int getTypeForMethod(int methodNo) {
		return Encode.decodeTypeNo(typeByMethod[methodNo]);
	}
	
	int [] getMethodsForTypeEncoded(int typeNo) {
		return methodsByType[typeNo];
	}
	
	int getMethodSignature(int methodNo) {
		return methodSignaturesByMethod[methodNo];
	}
	
	int getMethodNoBySignatureNo(int type, int signatureNo) {
		final long key = typeAndSignatureKey(type, signatureNo);

		final long methodNo = Hash.hashGet(typeAndMethodSignatureToMethodHash, key, HASH_UNDEF, TYPE_AND_METHOD_SIGNATURE_HASH);
		
		return methodNo != HASH_UNDEF ? (int)methodNo : -1;
	}
	
	int getIndexForMethod(int methodNo) {
		return indexByMethod[methodNo];
	}
	
	@FunctionalInterface
	interface GetSuperType {
		int getSuperType(int type);
	}
	
	
	int getDistinctMethodCount(int typeNo, MethodFilter methodFilter, GetSuperType getSuperType, VTableScratchArea scratchArea) {
		
		scratchArea.clear();

		final int superType = getSuperType.getSuperType(typeNo);
		
		int distinctMethodCount;
		
		if (superType != -1) {
			distinctMethodCount = getDistinctMethodCount(superType, methodFilter, getSuperType, scratchArea);
			distinctMethodCount += addDistinctMethods(typeNo, methodFilter, scratchArea);
		}
		else {
			distinctMethodCount = addDistinctMethods(typeNo, methodFilter, scratchArea);
		}
		
		return distinctMethodCount;
	}

	private int addDistinctMethods(int type, MethodFilter methodFilter, VTableScratchArea scratchArea) {
		
		int distinctMethods = 0;
		
		for (int encodedMethodNo : methodsByType[type]) {
			
			final int methodNo = Encode.decodeMethodNo(encodedMethodNo);
			final MethodVariant methodVariant = Encode.getMethodVariant(encodedMethodNo);
			
			if (!methodFilter.addMethod(methodNo, methodVariant)) {
				continue;
			}
			
			final int signatureNo = methodSignaturesByMethod[methodNo];
			
			if (!scratchArea.getAddedMethods().contains(signatureNo)) {
				
				scratchArea.add(type, indexByMethod[methodNo], distinctMethods);

				++ distinctMethods;
				
				scratchArea.getAddedMethods().add(encodedMethodNo);
			}
		}
		
		return distinctMethods;
	}
	
}
