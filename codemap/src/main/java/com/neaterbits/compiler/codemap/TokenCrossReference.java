package com.neaterbits.compiler.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class TokenCrossReference {

	private static final int SOURCEFILE_UNDEF = 0;
	
	private int sourceFileNo; // source file allocator
	private String [] sourceFiles;
	private final Map<String, Integer> sourceFileToIndex;
	
	private int tokenNo; // token allocator

	private int [] tokenToSourceFile;    // which source file does token belong to
	private int [][] sourceFileToTokens; // tokens, mapped by source file
	
	private long [] tokenToOffsetAndLength; // each token has offset and length, encoded in a long

	private final TokenReferenceMap variableReferences;
	private final TokenReferenceMap methodReferences;
	
	TokenCrossReference() {

		this.variableReferences = new TokenReferenceMap();
		this.methodReferences = new TokenReferenceMap();

		this.sourceFileToIndex = new HashMap<>();
		
		this.sourceFileNo = SOURCEFILE_UNDEF + 1;
		this.tokenNo = TokenReferenceMap.TOKEN_UNDEF + 1;
	}
	
	int addSourceFile(String file) {
		
		Objects.requireNonNull(file);
		
		if (!file.trim().equals(file)) {
			throw new IllegalArgumentException();
		}
		
		if (file.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		if (sourceFileToIndex.containsKey(file)) {
			throw new IllegalArgumentException();
		}
		
		final int sourceFileIdx = sourceFileNo ++;
	
		this.sourceFiles = ArrayAllocation.allocateArray(sourceFiles, ArrayAllocation.DEFAULT_LENGTH, String[]::new);
		
		sourceFiles[sourceFileIdx] = file;
		sourceFileToIndex.put(file, sourceFileIdx);
		
		return sourceFileIdx;
	}
	
	void removeSourceFile(String file) {
		
		Objects.requireNonNull(file);

		final Integer sourceFileIdx = sourceFileToIndex.remove(file);
		
		if (sourceFileIdx == null) {
			throw new IllegalStateException();
		}
		
		sourceFiles[sourceFileIdx] = null;
		
		for (int token : sourceFileToTokens[sourceFileIdx]) {
			removeTokenRefs(token);
		}
	}
	
	int addToken(int sourceFile, int tokenOffset, int tokenLength) {
		
		if (sourceFiles[sourceFile] == null) {
			throw new IllegalArgumentException();
		}
		
		if (tokenOffset < 0) {
			throw new IllegalArgumentException();
		}
		
		if (tokenLength <= 0) {
			throw new IllegalArgumentException();
		}
		
		final int tokenIdx = tokenNo ++;
		
		if (tokenIdx == TokenReferenceMap.TOKEN_UNDEF) {
			throw new IllegalStateException();
		}
		
		this.tokenToSourceFile = ArrayAllocation.allocateIntArray(tokenToSourceFile, ArrayAllocation.DEFAULT_LENGTH);
		tokenToSourceFile[tokenIdx] = sourceFile;

		this.tokenToOffsetAndLength = ArrayAllocation.allocateLongArray(tokenToOffsetAndLength, ArrayAllocation.DEFAULT_LENGTH);
		tokenToOffsetAndLength[tokenIdx] = tokenOffset << 32 | tokenLength;
		
		return tokenIdx;
	}
	
	void addTokenVariableReference(int fromToken, int toDeclarationToken) {
		variableReferences.addTokenReference(fromToken, toDeclarationToken);
	}
	
	void addTokenMethodReference(int fromToken, int toDeclarationToken) {
		methodReferences.addTokenReference(fromToken, toDeclarationToken);
	}
	
	int [] getTokensReferencingVariableDeclaration(int declarationToken) {
		return variableReferences.getTokensReferencingDeclaration(declarationToken);
	}
	
	int getVariableDeclarationTokenReferencedFrom(int fromToken) {
		return variableReferences.getDeclarationTokenReferencedFrom(fromToken);
	}

	int [] getTokensReferencingMethodDeclaration(int declarationToken) {
		return methodReferences.getTokensReferencingDeclaration(declarationToken);
	}

	int getDeclarationTokenReferencedFrom(int fromToken) {
		return variableReferences.getDeclarationTokenReferencedFrom(fromToken);
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
		
		tokenToSourceFile[token] = SOURCEFILE_UNDEF;
		tokenToOffsetAndLength[token] = -1L;

		if (variableReferences.hasToken(token)) {
			variableReferences.removeToken(token);
		}
		else if (methodReferences.hasToken(token)) {
			methodReferences.removeToken(token);
		}
	}
}
