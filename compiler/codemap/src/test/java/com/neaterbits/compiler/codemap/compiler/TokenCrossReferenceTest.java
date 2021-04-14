package com.neaterbits.compiler.codemap.compiler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TokenCrossReferenceTest {

	@Test
	public void testAddVariableTokenAndRemoveFile() {

		final TokenCrossReference tokenReference = new TokenCrossReference();

		final int fileA = 1; // tokenReference.addSourceFile("file_a");
		// assertThat(fileA).isEqualTo(1);

		final int variableDeclarationToken = tokenReference.addToken(fileA, 125);
		assertThat(variableDeclarationToken).isGreaterThanOrEqualTo(0);

		final int methodDeclarationToken = tokenReference.addToken(fileA, 129);
        assertThat(methodDeclarationToken).isEqualTo(variableDeclarationToken + 1);

		final int fileB = 2; // tokenReference.addSourceFile("file_b");
		// assertThat(fileB).isEqualTo(2);

		final int referenceToVariableToken = tokenReference.addToken(fileB, 54);
		assertThat(referenceToVariableToken).isEqualTo(methodDeclarationToken + 1);

		final int referenceToMethodToken = tokenReference.addToken(fileB, 59);
        assertThat(referenceToMethodToken).isEqualTo(referenceToVariableToken + 1);

		tokenReference.addTokenVariableReference(referenceToVariableToken, variableDeclarationToken);

		tokenReference.addTokenMethodReference(referenceToMethodToken, methodDeclarationToken);

		assertThat(tokenReference.getTokensReferencingVariableDeclaration(variableDeclarationToken))
					.isEqualTo(new int [] { referenceToVariableToken });

		assertThat(tokenReference.getVariableDeclarationTokenReferencedFrom(referenceToVariableToken)).isEqualTo(variableDeclarationToken);

        assertThat(tokenReference.getMethodDeclarationTokenReferencedFrom(referenceToMethodToken)).isEqualTo(methodDeclarationToken);

        assertThat(tokenReference.getTokensForSourceFile(fileA)).isEqualTo(new int [] { variableDeclarationToken, methodDeclarationToken });

		assertThat(tokenReference.getTokensForSourceFile(fileB)).isEqualTo(new int [] { referenceToVariableToken, referenceToMethodToken });

		tokenReference.removeFile(fileA);

        assertThat(tokenReference.getTokensReferencingVariableDeclaration(variableDeclarationToken))
                    .isNull();

        assertThat(tokenReference.getVariableDeclarationTokenReferencedFrom(referenceToVariableToken))
            .isEqualTo(-1);

        assertThat(tokenReference.getTokensReferencingMethodDeclaration(methodDeclarationToken))
            .isNull();

        assertThat(tokenReference.getMethodDeclarationTokenReferencedFrom(referenceToMethodToken))
.           isEqualTo(-1);

        assertThat(tokenReference.getSourceFileForToken(variableDeclarationToken)).isEqualTo(-1);

        assertThat(tokenReference.getTokensForSourceFile(fileA)).isNull();
	}

	@Test
	public void testRemoveToken() {

        final TokenCrossReference tokenReference = new TokenCrossReference();

        final int fileA = 1; // tokenReference.addSourceFile("file_a");
        // assertThat(fileA).isEqualTo(1);

        final int variableDeclarationToken = tokenReference.addToken(fileA, 125);
        assertThat(variableDeclarationToken).isGreaterThanOrEqualTo(0);

        assertThat(tokenReference.getTokensForSourceFile(fileA)).isEqualTo(new int [] { variableDeclarationToken });

        tokenReference.removeToken(variableDeclarationToken);

        assertThat(tokenReference.getTokensForSourceFile(fileA)).isNull();
	}

	@Test
    public void testAddRemoveParseTreeRef() {

        final TokenCrossReference tokenReference = new TokenCrossReference();

        final int fileA = 1; // tokenReference.addSourceFile("file_a");
        // assertThat(fileA).isEqualTo(1);

        final int variableDeclarationToken = tokenReference.addToken(fileA, 125);
        assertThat(variableDeclarationToken).isGreaterThanOrEqualTo(0);

        assertThat(tokenReference.getParseTreeRefForToken(variableDeclarationToken)).isEqualTo(125);
        assertThat(tokenReference.getTokenForParseTreeRef(fileA, 125)).isEqualTo(variableDeclarationToken);
    }
}
