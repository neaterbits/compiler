package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.compiler.common.loader.TypeVariant;

import static com.neaterbits.compiler.common.resolver.codemap.Encode.encodeType;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.decodeTypeNo;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.getTypeVariant;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.decodeMethodNo;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.getMethodVariant;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.encodeMethod;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.encodeMethodWithMethodVariant;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.encodeMethodWithoutTypeVariant;

import static com.neaterbits.compiler.common.resolver.codemap.Hash.GetCompareValue;

import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateIntArray;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateLongArray;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateArray;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.addToSubIntArray;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateSubArray;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.subIntArraySize;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.subIntArrayCopy;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.subIntArrayInitialIndex;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.subIntArrayLastIndex;


final class MethodMap extends BaseCodeMap {

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
	private long [] methodSignatures;
	

	
	private int [][] extendedMethodsByExtending; // Map from methodNo to an array of methods that are extended by this one
	private int [][] extendingMethodsByExtended;  // Map from methodNo to an array of methods that are extending this one
	
	private int [][] methodsByType;

	private int [] typeByMethod;		// Find type from methodNo
	private int [] methodSignaturesByMethod; // Find unique signature from methodNo
	
	// hashmap as simple long [] array
	// for finding methodno for a particular type/method signature combination
	// entries are 20 bits for typeno, 20 bits for signatureno and 24 bits for methodno
	private long [] typeAndMethodSignatureToMethodHash;

	private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;
	
	MethodMap() {
		this.typeAndMethodSignatureToMethodHash = Hash.makeHashMap(10000, HASH_UNDEF);
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
			
			this.methodSignatures = allocateLongArray(methodSignatures, methodSignatureIndex + 1);
			methodSignatures[methodSignatureIndex] = nameIndex.longValue() << 32 | paramsIndex.longValue();
			
			cache.storeSignatureNo(nameIndex, paramsIndex, methodSignatureIndex);
		}
		
		return methodSignatureIndex;
	}
	
	void allocateMethods(int typeNo, int numMethods) {
		this.methodsByType = allocateIntArray(methodsByType, typeNo + 1);
		
		this.methodsByType[typeNo] = allocateSubArray(numMethods);
	}
	
	int addMethod(int typeNo, TypeVariant typeVariant, String name, int [] parameters, MethodVariant methodVariant, MethodMapCache cache) {
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
		
		this.methodsByType = allocateIntArray(this.methodsByType, typeNoEncoded + 1, false);
		addToSubIntArray(methodsByType, typeNoEncoded, encodedMethod, 5);
		
		this.typeByMethod = allocateIntArray(this.typeByMethod, numMethods);
		this.typeByMethod[methodIndex] = typeNoEncoded;
		
		this.extendedMethodsByExtending = allocateIntArray(extendedMethodsByExtending, numMethods);
		this.extendingMethodsByExtended = allocateIntArray(extendingMethodsByExtended, numMethods);

		return methodIndex;
	}
	
	void addTypeExtendsTypes(int extendingTypeEncoded, int [] extendedTypesEncoded) {
		
		final int extendingType = decodeTypeNo(extendingTypeEncoded);
		final int [] extendingMethodsEncoded = methodsByType[extendingType];
		
		for (int i = subIntArrayInitialIndex(extendingMethodsEncoded); i <= subIntArrayLastIndex(extendingMethodsEncoded); ++ i) {
			
			final int extendingMethodEncoded = extendingMethodsEncoded[i];
			
			final int extendingMethod = decodeMethodNo(extendingMethodEncoded);
			
			final int methodSignatureNo = methodSignaturesByMethod[extendingMethod];
			
			for (int extendedTypeEncoded : extendedTypesEncoded) {
				
				final int extendedType = decodeTypeNo(extendedTypeEncoded);
				final long key = typeAndSignatureKey(extendedType, methodSignatureNo);

				final long extendedMethodNoWithoutTypeVariant = Hash.hashGet(typeAndMethodSignatureToMethodHash, key, HASH_UNDEF, TYPE_AND_METHOD_SIGNATURE_HASH);
				
				if (extendedMethodNoWithoutTypeVariant != HASH_UNDEF) {
					final TypeVariant extendedTypeVariant = getTypeVariant(extendedTypeEncoded);

					addMethodExtends(
							encodeMethodWithMethodVariant(
									(int)extendedMethodNoWithoutTypeVariant,
									extendedTypeVariant),
							
							extendingMethodEncoded);
				}
			}
		}
	}
	
	private static void checkHasNonStaticMethodVariant(int methodWithMethodVariant) {
		if (getMethodVariant(methodWithMethodVariant) == MethodVariant.STATIC) {
			throw new IllegalStateException();
		}
	}
	
	// Add a method directly overriding another one, in a class or interface
	private void addMethodExtends(int extendedMethodEncoded, int extendingMethodEncoded) {

		final int extendedMethod  = decodeMethodNo(extendedMethodEncoded);
		final int extendingMethod = decodeMethodNo(extendingMethodEncoded);

		checkHasNonStaticMethodVariant(extendedMethodEncoded);
		checkHasNonStaticMethodVariant(extendingMethodEncoded);

		addToSubIntArray(extendingMethodsByExtended, extendedMethod, 	extendingMethodEncoded, 3);
		addToSubIntArray(extendedMethodsByExtending, extendingMethod, 	extendedMethodEncoded, 3);
	}
	
	int getNumberOfMethodsDirectlyExtending(int methodNo) {
		return this.extendingMethodsByExtended[methodNo] != null ? subIntArraySize(this.extendingMethodsByExtended, methodNo) : 0;
	}
	
	int [] getMethodsDirectlyExtending(int methodNo) {
		return this.extendingMethodsByExtended[methodNo] != null ? subIntArrayCopy(this.extendingMethodsByExtended[methodNo]) : null;
	}
	

	int getNumberOfMethodsDirectlyExtendedBy(int methodNo) {
		return this.extendedMethodsByExtending[methodNo] != null ? subIntArraySize(this.extendedMethodsByExtending, methodNo) : 0;
	}

	int [] getMethodsDirectlyExtendedBy(int methodNo) {
		
		return this.extendedMethodsByExtending[methodNo] != null ? subIntArrayCopy(this.extendedMethodsByExtending[methodNo]) : null;
	}
	
	int getTotalNumberOfMethodsExtending(int methodNo) {

		int count = 0;
		
		if (this.extendedMethodsByExtending[methodNo] != null) {
			final int subSize = subIntArraySize(extendedMethodsByExtending, methodNo);

			for (int i = 0; i < subSize; ++ i) {
				count += getTotalNumberOfMethodsExtending(extendedMethodsByExtending[methodNo][i + 1]);
			}
		}
		
		return count;
	}
	
	String getMethodName(int methodNo) {
		return methodNames[(int)(methodSignatures[methodNo] >> 32)];
	}
	
	int [] getMethodParameterTypes(int methodNo) {
		final int [] types = parameterSignatures[(int)(methodSignatures[methodNo] & 0xFFFFFFFF)];

		return types != null ? Arrays.copyOf(types, types.length) : null;
	}
}
