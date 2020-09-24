package com.neaterbits.compiler.codemap;

import static com.neaterbits.compiler.codemap.ArrayAllocation.addToSubIntArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateIntArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateSubArray;
import static com.neaterbits.compiler.codemap.Encode.decodeMethodNo;
import static com.neaterbits.compiler.codemap.Encode.encodeMethod;
import static com.neaterbits.compiler.codemap.Encode.encodeMethodWithoutTypeVariant;
import static com.neaterbits.compiler.codemap.Encode.encodeType;
import static com.neaterbits.compiler.codemap.Encode.getMethodVariant;

import java.util.Objects;

import com.neaterbits.compiler.codemap.CodeMap.MethodFilter;
import com.neaterbits.compiler.types.MethodInfo;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.util.Hash;
import com.neaterbits.compiler.util.Hash.GetCompareValue;

final class MethodMap {

	// Sequence number for methods
	// Each method has their own methodNo even if same signature
	// (eg. multiple implementations of same interface)
	private int methodNo;

	private int [][] methodsByType;

	private int [] typeByMethod;		// Find type from methodNo
	private int [] indexByMethod;		// Index into type by methodNo
	private int [] methodSignaturesByMethod; // Find unique signature from methodNo

	// hashmap as simple long [] array
	// for finding methodno for a particular type/method signature combination
	// entries are 20 bits for typeno, 20 bits for signatureno and 24 bits for methodno
	private long [] typeAndMethodSignatureToMethodHash;

	private final CallablesSignatureMap signatureMap;

	private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;

	MethodMap() {
		this.typeAndMethodSignatureToMethodHash = Hash.makeHashMap(10000, HASH_UNDEF);

		this.signatureMap = new CallablesSignatureMap();
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


	void allocateMethods(int typeNo, int numMethods) {
		this.methodsByType = allocateIntArray(methodsByType, typeNo + 1);

		this.methodsByType[typeNo] = allocateSubArray(numMethods);
	}

	int addMethod(
	        int typeNo,
	        TypeVariant typeVariant,
	        String name,
	        int [] parameters,
	        MethodVariant methodVariant,
	        int indexInType) {

	    Objects.requireNonNull(name);
		Objects.requireNonNull(parameters);

		// Allocate new method identifier
		final int methodIndex = methodNo ++;
		final int numMethods = methodNo;

		final int signatureNo = signatureMap.findOrAddSignature(name, parameters);

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

	MethodInfo getMethodInfo(int typeNo, String methodName, int [] parameterTypes) {

		final Integer nameIndex = signatureMap.getCallableNameNo(methodName);

		final MethodInfo methodInfo;

		if (nameIndex == null) {
			methodInfo = null;
		}
		else {

			final Integer paramIndex = signatureMap.getParamTypesNo(parameterTypes);

			if (paramIndex == null) {
				methodInfo = null;
			}
			else {
				final long signatureNo = signatureMap.getCallableSignatureNo(nameIndex, paramIndex);

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

	int getMethodNo(int typeNo, String methodName, int [] parameterTypes) {

		final MethodInfo methodInfo = getMethodInfo(typeNo, methodName, parameterTypes);

		return methodInfo != null ? methodInfo.getMethodNo() : -1;
	}

	String getMethodName(int methodNo) {
		return signatureMap.getCallableName(methodNo);
	}

	int [] getMethodParameterTypes(int methodNo) {

		final int signatureIndex = methodSignaturesByMethod[methodNo];

		return signatureMap.getSignatureParameterTypes(signatureIndex);
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

		final int [] subArray = methodsByType[type];

		final int initial = ArrayAllocation.subIntArrayInitialIndex(subArray);
        final int last = ArrayAllocation.subIntArrayLastIndex(subArray);

		for (int i = initial; i <= last; ++ i) {

		    final int encodedMethodNo = subArray[i];

			final int methodNo = Encode.decodeMethodNo(encodedMethodNo);

			final MethodVariant methodVariant = Encode.getMethodVariant(encodedMethodNo);

			if (!methodFilter.addMethod(methodNo, methodVariant)) {
				continue;
			}

			final int signatureNo = methodSignaturesByMethod[methodNo];

			if (!scratchArea.hasAddedMethod(signatureNo)) {

				scratchArea.add(type, signatureNo, indexByMethod[methodNo], distinctMethods);

				++ distinctMethods;
			}
		}

		return distinctMethods;
	}
}
