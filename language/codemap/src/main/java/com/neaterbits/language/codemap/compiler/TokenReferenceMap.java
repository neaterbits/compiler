package com.neaterbits.language.codemap.compiler;

import com.neaterbits.language.codemap.ArrayAllocation;
import com.neaterbits.util.Bits;
import com.neaterbits.util.Hash;
import com.neaterbits.util.Hash.GetCompareValue;

// Cross referencing token names, eg. for rename
final class TokenReferenceMap {

	private int [][] referencedFromDeclaration; // eg references to a variable declaration from other places

	private long [] referenceToDeclarationMap;

    private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;

    private static final long TOKEN_VALUE_MASK = Bits.mask(BitDefs.TOKEN_BITS, 0);
    private static final int TOKEN_KEY_SHIFT = BitDefs.TOKEN_BITS;

    private static final GetCompareValue REFERENCE_TOKEN_TO_DECLARATION_HASH = new GetCompareValue() {

        @Override
        public long makeMapValue(long key, long value) {

            return key << TOKEN_KEY_SHIFT | value;
        }

        @Override
        public long getValue(long mapValue) {
            return mapValue & TOKEN_VALUE_MASK;
        }

        @Override
        public long getKey(long mapValue) {
            return mapValue >>> TOKEN_KEY_SHIFT;
        }

        @Override
        public long getDefaultValue() {
            return HASH_UNDEF;
        }
    };

	/*
	private static final int MAX_INFO_BITS = 32 - BitDefs.TOKEN_BITS;
	private static final int MAX_INFO_VALUE = (1 << MAX_INFO_BITS) - 1;

	static {
		if (MAX_INFO_BITS <= 0 + BitDefs.WARNING_WORKAROUND) {
			throw new IllegalStateException();
		}
	}
	*/

    TokenReferenceMap() {
        this.referenceToDeclarationMap = Hash.makeHashMap(10000, HASH_UNDEF);
    }

	void addTokenReference(int fromToken, int toDeclarationToken) {

		this.referenceToDeclarationMap = Hash.hashStore(
		        referenceToDeclarationMap,
		        makeReferenceToDeclarationKey(fromToken),
		        toDeclarationToken,
		        HASH_UNDEF,
		        REFERENCE_TOKEN_TO_DECLARATION_HASH);

		this.referencedFromDeclaration = ArrayAllocation.allocateIntArray(referencedFromDeclaration, ArrayAllocation.DEFAULT_LENGTH);

		ArrayAllocation.addToSubIntArray(
				referencedFromDeclaration,
				toDeclarationToken,
				fromToken,
				ArrayAllocation.DEFAULT_LENGTH);
	}

	private static long makeReferenceToDeclarationKey(int token) {

        return token;
    }

	boolean hasDeclarationToken(int token) {

		return   (referencedFromDeclaration != null && referencedFromDeclaration[token] != null);
	}

	boolean hasReferenceToken(int token) {

	    return Hash.hashGet(
		                     referenceToDeclarationMap,
		                     makeReferenceToDeclarationKey(token),
		                     HASH_UNDEF,
		                     REFERENCE_TOKEN_TO_DECLARATION_HASH) != HASH_UNDEF;
	}

	int [] getTokensReferencingDeclaration(int declarationToken) {
		return
		           referencedFromDeclaration[declarationToken] == null
		        || ArrayAllocation.subIntArraySize(referencedFromDeclaration, declarationToken) == 0

		            ? null
                    : ArrayAllocation.subIntArrayValues(referencedFromDeclaration, declarationToken);
	}

	int getDeclarationTokenReferencedFrom(int fromReferenceToken) {

	    final long value = Hash.hashGet(
                referenceToDeclarationMap,
                makeReferenceToDeclarationKey(fromReferenceToken),
                HASH_UNDEF,
                REFERENCE_TOKEN_TO_DECLARATION_HASH);

	    return value != HASH_UNDEF ? (int)value : -1;
	}

	void removeDeclarationToken(int declarationToken) {

	    final int [] referencedFromTokens = referencedFromDeclaration[declarationToken];

	    final int initial = ArrayAllocation.subIntArrayInitialIndex(referencedFromTokens);
	    final int last = ArrayAllocation.subIntArrayLastIndex(referencedFromTokens);

	    for (int i = initial; i <= last; ++ i) {

	        final int referenceToken = referencedFromTokens[i];

	        Hash.hashRemove(
	                referenceToDeclarationMap,
	                makeReferenceToDeclarationKey(referenceToken),
	                HASH_UNDEF,
	                REFERENCE_TOKEN_TO_DECLARATION_HASH);
	    }

	    referencedFromDeclaration[declarationToken] = null;
	}

    void removeReferenceToken(int token) {

        final int declarationToken = getDeclarationTokenReferencedFrom(token);

        if (declarationToken >= 0) {
            ArrayAllocation.removeDistinctFromSubIntArray(referencedFromDeclaration, declarationToken, token);
        }

        Hash.hashRemove(
                referenceToDeclarationMap,
                makeReferenceToDeclarationKey(token),
                HASH_UNDEF,
                REFERENCE_TOKEN_TO_DECLARATION_HASH);
    }
}
