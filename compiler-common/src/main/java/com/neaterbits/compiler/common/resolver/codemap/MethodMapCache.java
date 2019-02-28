package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import static com.neaterbits.compiler.common.resolver.codemap.Hash.GetCompareValue;

// Utilized while building integer maps, reusing same index for same names or signatures
public final class MethodMapCache extends BaseCodeMap {
	
	private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;

	private long [] nameAndParamToSignatureNoHash;
	
	private final Map<Parameters, Integer> paramsToIndex;
	private final Map<String, Integer> nameToIndex;
	
	public MethodMapCache() {
		this.paramsToIndex = new HashMap<>();
		this.nameToIndex = new HashMap<>();
		
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

	int getSignatureNo(int paramNameNo, int paramTypesNo) {
		final long value = Hash.hashGet(nameAndParamToSignatureNoHash, nameAndTypesKey(paramNameNo, paramTypesNo), HASH_UNDEF, NAME_AND_PARAMS_AND_SIGNATURENO_HASH);
		
		return value != HASH_UNDEF ? (int)value : -1;
	}
	
	void storeSignatureNo(int paramNameNo, int paramTypesNo, int signatureNo) {
		this.nameAndParamToSignatureNoHash = Hash.hashStore(
				nameAndParamToSignatureNoHash,
				nameAndTypesKey(paramNameNo, paramTypesNo),
				signatureNo,
				HASH_UNDEF,
				NAME_AND_PARAMS_AND_SIGNATURENO_HASH);
	}
	
	private static long nameAndTypesKey(long paramNameNo, long paramTypesNo) {
		return paramNameNo << Encode.PARAM_TYPES_BITS | paramTypesNo;
	}
	
	Integer getParamsIndex(int [] paramTypes) {
		return paramsToIndex.get(new Parameters(paramTypes));
	}
	
	void addParamsIndex(int [] paramTypes, int index) {
		paramsToIndex.put(new Parameters(paramTypes), index);
	}
	
	Integer getNameIndex(String name) {
		return nameToIndex.get(name);
	}
	
	void addNameIndex(String name, int index) {
		nameToIndex.put(name, index);
	}

	private static class Parameters {
		private final int [] paramTypes;

		Parameters(int [] paramTypes) {
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
			Parameters other = (Parameters) obj;
			if (!Arrays.equals(paramTypes, other.paramTypes))
				return false;
			return true;
		}
	}
}
