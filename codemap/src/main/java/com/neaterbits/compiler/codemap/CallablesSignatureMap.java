package com.neaterbits.compiler.codemap;

import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateIntArray;
import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateLongArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.neaterbits.compiler.util.Hash;
import com.neaterbits.compiler.util.Hash.GetCompareValue;

/**
 * Maps from callable names and parameter types to signature no
 * in order to reuse signatures e.g. for method overrides.
 *
 */

public final class CallablesSignatureMap {

	private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;

	private long [] nameAndParamToSignatureNoHash;

	// Each unique callable name is given a sequence number
    private int callableNameNoGenerator;
    private String [] callableNames;

    // Each unique signature set of types is given a sequence number that is used as index here so that
    // we can reuse distinct signatures
    private int parameterSignatureNoGenerator;
    private int [][] parameterSignatures;

    // Each unique name and type combination is given a sequence number
    private int callableSignatureNoGenerator;

    // Unique callable signatures, 32 bits for name and 32 bits for parameters
    private long [] callableSignaturesBySignatureIndex;

	private final Map<ParameterTypes, Integer> paramsToIndex;
	private final Map<String, Integer> callableNameToIndex;

	public CallablesSignatureMap() {
		this.paramsToIndex = new HashMap<>();
		this.callableNameToIndex = new HashMap<>();

		this.nameAndParamToSignatureNoHash = Hash.makeHashMap(10000, HASH_UNDEF);
	}

	private static final GetCompareValue NAME_AND_PARAMS_AND_SIGNATURENO_HASH = new GetCompareValue() {
		@Override
		public long makeMapValue(long key, long value) {

			if (value > Encode.MAX_SIGNATURENO) {
				throw new IllegalArgumentException();
			}

			return key << Encode.SIGNATURENO_BITS | value;
		}
		@Override
		public long getValue(long mapValue) {
			return mapValue & ((1 << Encode.SIGNATURENO_BITS) - 1);
		}

		@Override
		public long getKey(long mapValue) {
			return mapValue >>> Encode.SIGNATURENO_BITS;
		}

		@Override
		public long getDefaultValue() {
			return HASH_UNDEF;
		}
	};

	/**
	 * Get name of a callable from a callable no
	 *
	 * @param callableNo callable no
	 *
	 * @return name of callable
	 */
    String getCallableName(int callableNo) {
        return callableNames[(int)(callableSignaturesBySignatureIndex[callableNo] >> 32)];
    }

    /**
     * Get parameter types from a signature
     * @param signatureNo
     *
     * @return parameter types
     */
    int [] getSignatureParameterTypes(int signatureNo) {

        if (signatureNo >= callableSignatureNoGenerator) {
            throw new IllegalArgumentException();
        }

        final int [] types = parameterSignatures[(int)(callableSignaturesBySignatureIndex[signatureNo] & 0xFFFFFFFF)];

        return types;
    }

    /**
     * Adds or retrieves existing signature no for a callable name and parameter types.
     *
     * @param callableName name of callable
     * @param parameterTypes types of parameters
     *
     * @return existing or new signature no
     */
    final int findOrAddSignature(String callableName, int [] parameterTypes) {

        Integer paramsIndex = getParamTypesNo(parameterTypes);

        if (paramsIndex == null) {

            this.parameterSignatures = allocateIntArray(parameterSignatures, parameterSignatureNoGenerator + 1);

            paramsIndex = parameterSignatureNoGenerator ++;

            this.parameterSignatures[paramsIndex] = parameterTypes;

            addParamTypesNo(parameterTypes, paramsIndex);
        }

        Integer callableNameNo = getCallableNameNo(callableName);

        if (callableNameNo == null) {
            this.callableNames = allocateArray(callableNames, callableNameNoGenerator + 1, length -> new String[length]);

            callableNameNo = callableNameNoGenerator ++;

            this.callableNames[callableNameNo] = callableName;

            addCallableNameIndex(callableName, callableNameNo);
        }

        int callableSignatureIndex = getCallableSignatureNo(callableNameNo, paramsIndex);

        if (callableSignatureIndex < 0) {
            callableSignatureIndex = callableSignatureNoGenerator ++;

            this.callableSignaturesBySignatureIndex = allocateLongArray(callableSignaturesBySignatureIndex, callableSignatureIndex + 1);

            callableSignaturesBySignatureIndex[callableSignatureIndex] = callableNameNo.longValue() << 32 | paramsIndex.longValue();

            storeCallableSignatureNo(callableNameNo, paramsIndex, callableSignatureIndex);
        }

        return callableSignatureIndex;
    }

	/**
	 * Retrieves a mapping from callable name and parameter types to callable signature no.
	 *
	 * @param callableNameNo registered callable no
	 * @param paramTypesNo registered parameter types no
	 *
	 * @return callable signature no or -1 if not found
	 */
	int getCallableSignatureNo(int callableNameNo, int paramTypesNo) {

	    final long value = Hash.hashGet(
		        nameAndParamToSignatureNoHash,
		        callableNameAndTypesKey(callableNameNo, paramTypesNo),
		        HASH_UNDEF,
		        NAME_AND_PARAMS_AND_SIGNATURENO_HASH);

		return value != HASH_UNDEF ? (int)value : -1;
	}

    /**
     * Stores a mapping from callable name and parameter types to a callable signature.
     *
     * @param callableNameNo registered callable name
     * @param paramTypesNo registered parameter types
     * @param signatureNo callable signature number
     *
     */
	private void storeCallableSignatureNo(int callableNameNo, int paramTypesNo, int signatureNo) {
		this.nameAndParamToSignatureNoHash = Hash.hashStore(
				nameAndParamToSignatureNoHash,
				callableNameAndTypesKey(callableNameNo, paramTypesNo),
				signatureNo,
				HASH_UNDEF,
				NAME_AND_PARAMS_AND_SIGNATURENO_HASH);
	}

	private static long callableNameAndTypesKey(long paramsCallableNameNo, long paramTypesNo) {
		return paramsCallableNameNo << Encode.PARAM_TYPES_BITS | paramTypesNo;
	}

	/**
	 * Retrieves callable parameter types shared between callables based on array of types.
	 *
	 * @param paramTypes array of registered types
	 *
	 * @return param types no for types, or null if not stored
	 */
	Integer getParamTypesNo(int [] paramTypes) {
		return paramsToIndex.get(new ParameterTypes(paramTypes));
	}

	/**
	 * Adds a list of parameter types shared between callables.
	 *
	 * @param paramTypes array of parameters
	 * @param paramTypesNo an index allocated for parameter shared parameter types
	 */
	private void addParamTypesNo(int [] paramTypes, int paramTypesNo) {
		paramsToIndex.put(new ParameterTypes(paramTypes), paramTypesNo);
	}

	/**
	 * Retrieves mapping from callable name to sequence no
	 *
	 * @param callableName the callable name
	 *
	 * @return callable name no
	 */
	Integer getCallableNameNo(String callableName) {
		return callableNameToIndex.get(callableName);
	}

	/**
	 * Add index for a callable name
	 *
	 * @param callableName callable namne
	 * @param index callable name no
	 */
	private void addCallableNameIndex(String callableName, int index) {
		callableNameToIndex.put(callableName, index);
	}

	private static class ParameterTypes {
		private final int [] paramTypes;

		ParameterTypes(int [] paramTypes) {
			this.paramTypes = paramTypes;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(paramTypes);
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
			ParameterTypes other = (ParameterTypes) obj;
			if (!Arrays.equals(paramTypes, other.paramTypes))
				return false;
			return true;
		}
	}
}
