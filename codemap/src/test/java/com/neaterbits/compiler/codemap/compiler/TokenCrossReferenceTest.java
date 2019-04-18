package com.neaterbits.compiler.codemap.compiler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.codemap.compiler.TokenCrossReference;

public class TokenCrossReferenceTest {

	@Test
	public void testAddToken() {
		
		final TokenCrossReference tokenReference = new TokenCrossReference();
		
		final int fileA = 1; // tokenReference.addSourceFile("file_a");
		// assertThat(fileA).isEqualTo(1);
	
		final int declarationToken = tokenReference.addToken(fileA, 125, 23);
		assertThat(declarationToken).isEqualTo(1);
		
		final int fileB = 2; // tokenReference.addSourceFile("file_b");
		// assertThat(fileB).isEqualTo(2);

		final int referenceToDeclarationToken = tokenReference.addToken(fileB, 54, 23);
		assertThat(referenceToDeclarationToken).isEqualTo(2);

		tokenReference.addTokenVariableReference(referenceToDeclarationToken, declarationToken);
		
		assertThat(tokenReference.getTokensReferencingVariableDeclaration(declarationToken))
					.isEqualTo(new int [] { referenceToDeclarationToken });
		
		assertThat(tokenReference.getDeclarationTokenReferencedFrom(referenceToDeclarationToken)).isEqualTo(declarationToken);
	}
}
