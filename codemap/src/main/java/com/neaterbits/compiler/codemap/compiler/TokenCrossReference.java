package com.neaterbits.compiler.codemap.compiler;

import com.neaterbits.compiler.codemap.ArrayAllocation;
import com.neaterbits.compiler.util.Bits;
import com.neaterbits.compiler.util.Hash;
import com.neaterbits.compiler.util.Hash.GetCompareValue;
import com.neaterbits.compiler.util.model.CrossReferenceGetters;

public final class TokenCrossReference implements CrossReferenceUpdater, CrossReferenceGetters {
	
	private int tokenNo; // token allocator

	private int [] tokenToSourceFile;    // which source file does token belong to
	private int [][] sourceFileToTokens; // tokens, mapped by source file
	
	private int [] parseTreeRefs; // reference into parse tree 
	private final long [] parseTreeRefToTokenMap;
	
	private final TokenReferenceMap variableReferences;
	private final TokenReferenceMap methodReferences;
	
	private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;
	
	private static final long VALUE_MASK = Bits.mask(BitDefs.TOKEN_BITS, 0);
	private static final int KEY_SHIFT = BitDefs.TOKEN_BITS;
	
	private static final GetCompareValue PARSE_TREE_REF_HASH = new GetCompareValue() {
		
		@Override
		public long makeMapValue(long key, long value) {

			return key << KEY_SHIFT | value;
		}
		
		@Override
		public long getValue(long mapValue) {
			return mapValue & VALUE_MASK;
		}
		
		@Override
		public long getKey(long mapValue) {
			return mapValue >>> KEY_SHIFT;
		}
		
		@Override
		public long getDefaultValue() {
			return HASH_UNDEF;
		}
	};
	
	TokenCrossReference() {

		this.variableReferences = new TokenReferenceMap();
		this.methodReferences = new TokenReferenceMap();

		this.tokenNo = TokenReferenceMap.TOKEN_UNDEF + 1;
		
		this.parseTreeRefToTokenMap = Hash.makeHashMap(10000, HASH_UNDEF);
	}
	
	@Override
	public int addToken(int sourceFile, int parseTreeRef) {

		System.out.println("## addToken " + parseTreeRef);

		checkRanges(sourceFile, parseTreeRef);
		
		if (tokenNo >= BitDefs.MAX_TOKEN_VALUE) {
			throw new IllegalStateException();
		}
		
		if (parseTreeRef == -1) {
			throw new IllegalArgumentException();
		}
		
		final int tokenIdx = tokenNo ++;
		
		if (tokenIdx == TokenReferenceMap.TOKEN_UNDEF) {
			throw new IllegalStateException();
		}
		
		this.tokenToSourceFile = ArrayAllocation.allocateIntArray(tokenToSourceFile, ArrayAllocation.DEFAULT_LENGTH);
		tokenToSourceFile[tokenIdx] = sourceFile;

		this.parseTreeRefs = ArrayAllocation.allocateIntArray(parseTreeRefs, ArrayAllocation.DEFAULT_LENGTH);
		parseTreeRefs[tokenIdx] = parseTreeRef;
		
		final long key = makeKey(sourceFile, parseTreeRef);
		Hash.hashStore(parseTreeRefToTokenMap, key, tokenIdx, HASH_UNDEF, PARSE_TREE_REF_HASH);
		
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
	
	
	private long makeKey(int sourceFile, int parseTreeRef) {
		
		final long key = (sourceFile << BitDefs.PARSE_TREE_REF_BITS) | parseTreeRef;

		return key;
	}
	
	@Override
	public int getTokenForParseTreeRef(int sourceFile, int parseTreeRef) {
		
		checkRanges(sourceFile, parseTreeRef);
		
		final long key = makeKey(sourceFile, parseTreeRef);
		
		final long value = Hash.hashGet(parseTreeRefToTokenMap, key, HASH_UNDEF, PARSE_TREE_REF_HASH);
		
		System.out.println("## got value " + value);
		
		return (int)value;
	}

	@Override
	public int getParseTreeRefForToken(int token) {

		return parseTreeRefs[token];
	}

	@Override
	public void addTokenVariableReference(int fromToken, int toDeclarationToken) {
	
		System.out.println("## addTokenVariableReference from " + fromToken + " to " + toDeclarationToken);
		
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

	int getDeclarationTokenReferencedFrom(int fromToken) {
		return variableReferences.getDeclarationTokenReferencedFrom(fromToken);
	}

	void removeFile(int sourceFileIdx) {
		for (int token : sourceFileToTokens[sourceFileIdx]) {
			removeTokenRefs(token);
		}
	}        
	
	void removeToken(int token) {

		final int sourceFileNo = tokenToSourceFile[token];
		final int [] sourcefileTokens = sourceFileToTokens[sourceFileNo];
		
		TokenReferenceMap.removeToken(sourcefileTokens, token);
		
		removeTokenRefs(token);
		
		if (variableReferences.hasToken(token)) {
			variableReferences.removeToken(token);
		}
		else if (methodReferences.hasToken(token)) {
			methodReferences.removeToken(token);
		}
	}
	
	private void removeTokenRefs(int token) {
		
		tokenToSourceFile[token] = IntCompilerCodeMap.SOURCEFILE_UNDEF;
		parseTreeRefs[token] = -1;

		if (variableReferences.hasToken(token)) {
			variableReferences.removeToken(token);
		}
		else if (methodReferences.hasToken(token)) {
			methodReferences.removeToken(token);
		}
	}
}
