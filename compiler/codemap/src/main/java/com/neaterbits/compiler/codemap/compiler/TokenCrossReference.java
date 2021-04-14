package com.neaterbits.compiler.codemap.compiler;

import com.neaterbits.compiler.codemap.ArrayAllocation;
import com.neaterbits.compiler.util.Bits;
import com.neaterbits.compiler.util.Hash;
import com.neaterbits.compiler.util.Hash.GetCompareValue;
import com.neaterbits.compiler.util.model.CrossReferenceGetters;

public final class TokenCrossReference implements CrossReferenceUpdater, CrossReferenceGetters {

	private int tokenNo; // token allocator

	private long [] tokenToSourceFileMap;    // which source file does token belong to
	private int [][] sourceFileToTokens; // tokens, mapped by source file

	private int [] parseTreeRefs; // reference into parse tree
	private final long [] parseTreeRefToTokenMap;

	private final TokenReferenceMap variableReferences;
	private final TokenReferenceMap methodReferences;

	private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;

	private static final long FILE_NO_VALUE_MASK = Bits.mask(BitDefs.TOKEN_BITS, 0);
    private static final int FILE_NO_KEY_SHIFT = BitDefs.TOKEN_BITS;

	private static final long PARSE_TREE_REF_VALUE_MASK = Bits.mask(BitDefs.TOKEN_BITS, 0);
	private static final int PARSE_TREE_REF_KEY_SHIFT = BitDefs.TOKEN_BITS;

	private static final GetCompareValue TOKEN_TO_FILE_NO_HASH = new GetCompareValue() {

        @Override
        public long makeMapValue(long key, long value) {

            return key << FILE_NO_KEY_SHIFT | value;
        }

        @Override
        public long getValue(long mapValue) {
            return mapValue & FILE_NO_VALUE_MASK;
        }

        @Override
        public long getKey(long mapValue) {
            return mapValue >>> FILE_NO_KEY_SHIFT;
        }

        @Override
        public long getDefaultValue() {
            return HASH_UNDEF;
        }
    };

	private static final GetCompareValue PARSE_TREE_REF_HASH = new GetCompareValue() {

		@Override
		public long makeMapValue(long key, long value) {

			return key << PARSE_TREE_REF_KEY_SHIFT | value;
		}

		@Override
		public long getValue(long mapValue) {
			return mapValue & PARSE_TREE_REF_VALUE_MASK;
		}

		@Override
		public long getKey(long mapValue) {
			return mapValue >>> PARSE_TREE_REF_KEY_SHIFT;
		}

		@Override
		public long getDefaultValue() {
			return HASH_UNDEF;
		}
	};

	TokenCrossReference() {

		this.variableReferences = new TokenReferenceMap();
		this.methodReferences = new TokenReferenceMap();

		this.tokenNo = 0;

		this.sourceFileToTokens = ArrayAllocation.allocateIntArray(sourceFileToTokens, ArrayAllocation.DEFAULT_LENGTH);

		this.tokenToSourceFileMap = Hash.makeHashMap(10000, HASH_UNDEF);
		this.parseTreeRefToTokenMap = Hash.makeHashMap(10000, HASH_UNDEF);
	}

	@Override
	public int addToken(int sourceFile, int parseTreeRef) {

		checkRanges(sourceFile, parseTreeRef);

		if (tokenNo >= BitDefs.MAX_TOKEN_VALUE) {
			throw new IllegalStateException();
		}

		final int tokenIdx = tokenNo ++;

		final long tokenToSourceFileKey = makeTokenToSourceFileKey(tokenIdx);
		Hash.hashStore(tokenToSourceFileMap, tokenToSourceFileKey, sourceFile, HASH_UNDEF, TOKEN_TO_FILE_NO_HASH);

		this.parseTreeRefs = ArrayAllocation.allocateIntArray(parseTreeRefs, ArrayAllocation.DEFAULT_LENGTH);
		parseTreeRefs[tokenIdx] = parseTreeRef;

		final long sourceFileAndPareTreeRefToToken = makeSourceFileAndParseTreeRefToTokenKey(sourceFile, parseTreeRef);
		Hash.hashStore(parseTreeRefToTokenMap, sourceFileAndPareTreeRefToToken, tokenIdx, HASH_UNDEF, PARSE_TREE_REF_HASH);

		ArrayAllocation.addToSubIntArray(sourceFileToTokens, sourceFile, tokenIdx, ArrayAllocation.DEFAULT_LENGTH);

		return tokenIdx;
	}

	private void checkRanges(int sourceFile, int parseTreeRef) {

		if (sourceFile > BitDefs.MAX_SOURCE_FILE) {
			throw new IllegalArgumentException();
		}

		if (parseTreeRef > BitDefs.MAX_PARSE_TREE_REF) {
			throw new IllegalArgumentException();
		}
	}

	private static long makeTokenToSourceFileKey(int token) {
	    return token;
	}

	private static long makeSourceFileAndParseTreeRefToTokenKey(int sourceFile, int parseTreeRef) {

		final long key = (sourceFile << BitDefs.PARSE_TREE_REF_BITS) | parseTreeRef;

		return key;
	}

	@Override
	public int getTokenForParseTreeRef(int sourceFile, int parseTreeRef) {

		checkRanges(sourceFile, parseTreeRef);

		final long key = makeSourceFileAndParseTreeRefToTokenKey(sourceFile, parseTreeRef);

		final long value = Hash.hashGet(parseTreeRefToTokenMap, key, HASH_UNDEF, PARSE_TREE_REF_HASH);

		return (int)value;
	}

	@Override
	public int getParseTreeRefForToken(int token) {

		return parseTreeRefs[token];
	}

	@Override
	public void addTokenVariableReference(int fromToken, int toDeclarationToken) {

		variableReferences.addTokenReference(fromToken, toDeclarationToken);
	}

	public void addTokenMethodReference(int fromToken, int toDeclarationToken) {

	    methodReferences.addTokenReference(fromToken, toDeclarationToken);
	}

	int [] getTokensReferencingVariableDeclaration(int declarationToken) {

	    return variableReferences.getTokensReferencingDeclaration(declarationToken);
	}

	@Override
	public int getVariableDeclarationTokenReferencedFrom(int fromReferenceToken) {

	    return variableReferences.getDeclarationTokenReferencedFrom(fromReferenceToken);
	}

	int [] getTokensReferencingMethodDeclaration(int declarationToken) {

	    return methodReferences.getTokensReferencingDeclaration(declarationToken);
	}

	@Override
    public int getMethodDeclarationTokenReferencedFrom(int fromReferenceToken) {

	    return methodReferences.getDeclarationTokenReferencedFrom(fromReferenceToken);
    }

    void removeFile(int sourceFileIdx) {

	    if (sourceFileToTokens != null && sourceFileToTokens[sourceFileIdx] != null) {

	        final int [] tokens = sourceFileToTokens[sourceFileIdx];

	        final int initial = ArrayAllocation.subIntArrayInitialIndex(tokens);
	        final int last = ArrayAllocation.subIntArrayLastIndex(tokens);

	        for (int i = initial; i <= last; ++ i) {
    			removeOneTokenExceptSourceFileToTokens(tokens[i]);
    		}
	    }

	    sourceFileToTokens[sourceFileIdx] = null;
	}

	int getSourceFileForToken(int token) {

	    return (int)Hash.hashGet(
	            tokenToSourceFileMap,
	            makeTokenToSourceFileKey(token),
	            HASH_UNDEF,
	            TOKEN_TO_FILE_NO_HASH);
	}

	int [] getTokensForSourceFile(int sourceFile) {

	    final int [] tokens;

	    if (sourceFileToTokens[sourceFile] != null) {

	        if (ArrayAllocation.subIntArraySize(sourceFileToTokens, sourceFile) == 0) {
	            tokens = null;
	        }
	        else {
	            tokens = ArrayAllocation.subIntArrayValues(sourceFileToTokens, sourceFile);
	        }
	    }
	    else {
	        tokens = null;
	    }

	    return tokens;
	}

	void removeToken(int token) {

	    final int sourceFileIdx = getSourceFileForToken(token);

	    removeOneTokenExceptSourceFileToTokens(token);

	    ArrayAllocation.removeDistinctFromSubIntArray(sourceFileToTokens, sourceFileIdx, token);
	}

    private void removeOneTokenExceptSourceFileToTokens(int token) {

		Hash.hashRemove(
		        tokenToSourceFileMap,
		        makeTokenToSourceFileKey(token),
		        HASH_UNDEF,
		        TOKEN_TO_FILE_NO_HASH);

		removeTokenRefs(token);
	}

	private void removeTokenRefs(int token) {

	    if (token < 0) {
	        throw new IllegalArgumentException();
	    }

		parseTreeRefs[token] = -1;

		if (variableReferences.hasReferenceToken(token)) {
            variableReferences.removeReferenceToken(token);
		}
		else if (variableReferences.hasDeclarationToken(token)) {
			variableReferences.removeDeclarationToken(token);
		}
		else if (methodReferences.hasReferenceToken(token)) {
			methodReferences.removeReferenceToken(token);
		}
		else if (methodReferences.hasDeclarationToken(token)) {
			methodReferences.removeDeclarationToken(token);
		}
	}
}
