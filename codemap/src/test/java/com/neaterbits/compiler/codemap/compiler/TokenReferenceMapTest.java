package com.neaterbits.compiler.codemap.compiler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TokenReferenceMapTest {

    @Test
    public void testAddAndRemoveDeclarationToken() {

        final TokenReferenceMap tokenReferenceMap = new TokenReferenceMap();

        final int declarationToken = 1;
        final int referenceToken = 2;

        assertThat(tokenReferenceMap.hasDeclarationToken(declarationToken)).isFalse();
        assertThat(tokenReferenceMap.hasReferenceToken(referenceToken)).isFalse();

        tokenReferenceMap.addTokenReference(referenceToken, declarationToken);

        assertThat(tokenReferenceMap.getDeclarationTokenReferencedFrom(referenceToken))
            .isEqualTo(declarationToken);

        assertThat(tokenReferenceMap.getTokensReferencingDeclaration(declarationToken))
            .isEqualTo(new int [] { referenceToken });

        assertThat(tokenReferenceMap.hasDeclarationToken(declarationToken)).isTrue();
        assertThat(tokenReferenceMap.hasReferenceToken(referenceToken)).isTrue();

        tokenReferenceMap.removeDeclarationToken(declarationToken);

        assertThat(tokenReferenceMap.hasDeclarationToken(declarationToken)).isFalse();
        assertThat(tokenReferenceMap.hasReferenceToken(referenceToken)).isFalse();

        assertThat(tokenReferenceMap.getDeclarationTokenReferencedFrom(referenceToken)).isEqualTo(-1);
        assertThat(tokenReferenceMap.getTokensReferencingDeclaration(declarationToken)).isNull();
    }

    @Test
    public void testAddAndRemoveReferenceToken() {

        final TokenReferenceMap tokenReferenceMap = new TokenReferenceMap();

        final int declarationToken = 1;
        final int referenceToken = 2;

        assertThat(tokenReferenceMap.hasDeclarationToken(declarationToken)).isFalse();
        assertThat(tokenReferenceMap.hasReferenceToken(referenceToken)).isFalse();

        tokenReferenceMap.addTokenReference(referenceToken, declarationToken);

        assertThat(tokenReferenceMap.getDeclarationTokenReferencedFrom(referenceToken))
            .isEqualTo(declarationToken);

        assertThat(tokenReferenceMap.getTokensReferencingDeclaration(declarationToken))
            .isEqualTo(new int [] { referenceToken });

        assertThat(tokenReferenceMap.hasDeclarationToken(declarationToken)).isTrue();
        assertThat(tokenReferenceMap.hasReferenceToken(referenceToken)).isTrue();

        tokenReferenceMap.removeReferenceToken(referenceToken);

        assertThat(tokenReferenceMap.hasDeclarationToken(declarationToken)).isTrue();
        assertThat(tokenReferenceMap.hasReferenceToken(referenceToken)).isFalse();

        assertThat(tokenReferenceMap.getDeclarationTokenReferencedFrom(referenceToken)).isEqualTo(-1);
        assertThat(tokenReferenceMap.getTokensReferencingDeclaration(declarationToken)).isNull();
    }
}
