package com.neaterbits.compiler.codemap;

// Cross referencing token names, eg. for rename
final class TokenReferenceMap {
	
	static final int TOKEN_UNDEF = 0;
	
	
	private int [][] referencedFromTokens; // eg references to a variable declaration from other places
	
	private int [] referenceToToken; 

	void addTokenReference(int fromToken, int toDeclarationToken) {

		if (fromToken <= TOKEN_UNDEF) {
			throw new IllegalArgumentException();
		}
		
		if (toDeclarationToken <= TOKEN_UNDEF) {
			throw new IllegalArgumentException();
		}

		if (referenceToToken != null && referenceToToken[fromToken] != TOKEN_UNDEF) {
			throw new IllegalStateException();
		}
		
		this.referenceToToken = ArrayAllocation.allocateIntArray(referenceToToken, ArrayAllocation.DEFAULT_LENGTH);
		referenceToToken[fromToken] = toDeclarationToken;
		
		this.referencedFromTokens = ArrayAllocation.allocateIntArray(referencedFromTokens, ArrayAllocation.DEFAULT_LENGTH);
		ArrayAllocation.addToSubIntArray(
				referencedFromTokens,
				toDeclarationToken,
				fromToken,
				ArrayAllocation.DEFAULT_LENGTH);
	}

	boolean hasToken(int token) {
		if (token <= TOKEN_UNDEF) {
			throw new IllegalArgumentException();
		}

		return referencedFromTokens[token] != null || referenceToToken[token] != TOKEN_UNDEF;
	}

	int [] getTokensReferencingDeclaration(int declarationToken) {
		return ArrayAllocation.subIntArrayCopy(referencedFromTokens[declarationToken]);
	}

	int getDeclarationTokenReferencedFrom(int fromToken) {
		return referenceToToken[fromToken];
	}
	
	void removeToken(int token) {

		final int referenceTo = referenceToToken[token];
		
		if (referenceTo != TOKEN_UNDEF) {
			
			final int [] tokens = referencedFromTokens[referenceTo];
			
			removeToken(tokens, token);
		}
		
		referenceToToken[token] = TOKEN_UNDEF;
	}
	
	static void removeToken(int [] tokens, int token) {
		for (int i = 0; i < tokens.length; ++ i) {
			if (tokens[i] == token) {
				tokens[i] = TOKEN_UNDEF;
			}
		}
	}
}
