package com.neaterbits.compiler.codemap.compiler;

import com.neaterbits.compiler.codemap.ArrayAllocation;

// Cross referencing token names, eg. for rename
final class TokenReferenceMap {
	
	static final int TOKEN_UNDEF = 0;
	
	private int [][] referencedFromDeclaration; // eg references to a variable declaration from other places
	
	private int [] referenceToDeclaration; 

	/*
	private static final int MAX_INFO_BITS = 32 - BitDefs.TOKEN_BITS;
	private static final int MAX_INFO_VALUE = (1 << MAX_INFO_BITS) - 1;
	
	static {
		if (MAX_INFO_BITS <= 0 + BitDefs.WARNING_WORKAROUND) {
			throw new IllegalStateException();
		}
	}
	*/

	void addTokenReference(int fromToken, int toDeclarationToken) {

		if (fromToken <= TOKEN_UNDEF) {
			throw new IllegalArgumentException();
		}
		
		if (toDeclarationToken <= TOKEN_UNDEF) {
			throw new IllegalArgumentException();
		}
		

		if (referenceToDeclaration != null && referenceToDeclaration[fromToken] != TOKEN_UNDEF) {
			throw new IllegalStateException();
		}
		
		this.referenceToDeclaration = ArrayAllocation.allocateIntArray(referenceToDeclaration, ArrayAllocation.DEFAULT_LENGTH);
		referenceToDeclaration[fromToken] = toDeclarationToken;
		
		this.referencedFromDeclaration = ArrayAllocation.allocateIntArray(referencedFromDeclaration, ArrayAllocation.DEFAULT_LENGTH);
		ArrayAllocation.addToSubIntArray(
				referencedFromDeclaration,
				toDeclarationToken,
				fromToken,
				ArrayAllocation.DEFAULT_LENGTH);
	}

	boolean hasToken(int token) {
		if (token <= TOKEN_UNDEF) {
			throw new IllegalArgumentException();
		}

		return referencedFromDeclaration[token] != null || referenceToDeclaration[token] != TOKEN_UNDEF;
	}

	int [] getTokensReferencingDeclaration(int declarationToken) {
		return ArrayAllocation.subIntArrayCopy(referencedFromDeclaration[declarationToken]);
	}

	int getDeclarationTokenReferencedFrom(int fromReferenceToken) {
		return referenceToDeclaration[fromReferenceToken];
	}
	
	void removeToken(int token) {

		final int referenceTo = referenceToDeclaration[token];
		
		if (referenceTo != TOKEN_UNDEF) {
			
			final int [] tokens = referencedFromDeclaration[referenceTo];
			
			removeToken(tokens, token);
		}
		
		referenceToDeclaration[token] = TOKEN_UNDEF;
	}
	
	static void removeToken(int [] tokens, int token) {
		for (int i = 0; i < tokens.length; ++ i) {
			if (tokens[i] == token) {
				tokens[i] = TOKEN_UNDEF;
			}
		}
	}
}
